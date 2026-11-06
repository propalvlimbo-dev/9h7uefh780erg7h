#requires -version 5
param(
    [string] $Action = 'watch',
    [long]   $Value = 0,
    [int]    $IntervalMs = 1000,
    [string] $ArtPath = "$env:TEMP\rose_media_art.png",
    [string] $CommandPath = "$env:TEMP\rose_media_cmd.txt"
)

$ErrorActionPreference = 'Stop'

Add-Type -AssemblyName System.Runtime.WindowsRuntime

$asTaskMethod = ([System.WindowsRuntimeSystemExtensions].GetMethods() | Where-Object {
        $_.Name -eq 'AsTask' -and
        $_.GetParameters().Count -eq 1 -and
        $_.GetParameters()[0].ParameterType.Name -eq 'IAsyncOperation`1'
    })[0]

function Wait-Async($operation, $resultType) {
    $task = $asTaskMethod.MakeGenericMethod($resultType).Invoke($null, @($operation))
    if (-not $task.Wait(4000)) { throw 'WinRT operation timed out' }
    return $task.Result
}

$null = [Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager, Windows.Media.Control, ContentType = WindowsRuntime]
$null = [Windows.Media.Control.GlobalSystemMediaTransportControlsSessionMediaProperties, Windows.Media.Control, ContentType = WindowsRuntime]
$null = [Windows.Storage.Streams.IRandomAccessStreamWithContentType, Windows.Storage.Streams, ContentType = WindowsRuntime]

$asStreamMethod = [System.IO.WindowsRuntimeStreamExtensions].GetMethod(
    'AsStreamForRead', [type[]] @([Windows.Storage.Streams.IInputStream]))

$managerType = [Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager]
$manager = Wait-Async ($managerType::RequestAsync()) ($managerType)


$audioReady = $false
try {
    if (-not ([System.Management.Automation.PSTypeName]'RoseAudio').Type) {
        Add-Type -TypeDefinition @'
using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices;
using System.Text;

public static class RoseAudio {
    private const uint PROCESS_QUERY_LIMITED_INFORMATION = 0x1000;
    private const int CLSCTX_ALL = 23;
    private const int E_RENDER = 0;
    private const int E_MULTIMEDIA = 1;

    internal enum AudioSessionState {
        Inactive = 0,
        Active = 1,
        Expired = 2
    }

    [ComImport, Guid("BCDE0395-E52F-467C-8E3D-C4579291692E")]
    internal class MMDeviceEnumerator { }

    [ComImport, Guid("A95664D2-9614-4F35-A746-DE8DB63617E6"),
     InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]
    internal interface IMMDeviceEnumerator {
        [PreserveSig]
        int EnumAudioEndpoints(int dataFlow, int stateMask, out IntPtr devices);

        [PreserveSig]
        int GetDefaultAudioEndpoint(int dataFlow, int role, out IMMDevice device);
    }

    [ComImport, Guid("D666063F-1587-4E43-81F1-B948E807363F"),
     InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]
    internal interface IMMDevice {
        [PreserveSig]
        int Activate([MarshalAs(UnmanagedType.LPStruct)] Guid iid, int clsCtx,
            IntPtr activationParams, [MarshalAs(UnmanagedType.IUnknown)] out object result);
    }

    [ComImport, Guid("77AA99A0-1BD6-484F-8BC7-2C654C9A9B6F"),
     InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]
    internal interface IAudioSessionManager2 {
        [PreserveSig]
        int NotImplemented1();

        [PreserveSig]
        int NotImplemented2();

        [PreserveSig]
        int GetSessionEnumerator(out IAudioSessionEnumerator sessionEnum);
    }

    [ComImport, Guid("E2F5BB11-0570-40CA-ACDD-3AA01277DEE8"),
     InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]
    internal interface IAudioSessionEnumerator {
        [PreserveSig]
        int GetCount(out int sessionCount);

        [PreserveSig]
        int GetSession(int sessionIndex, out IAudioSessionControl2 session);
    }

    [ComImport, Guid("BFB7FF88-7239-4FC9-8FA2-07C950BE9C6D"),
     InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]
    internal interface IAudioSessionControl2 {
        [PreserveSig]
        int GetState(out AudioSessionState state);

        [PreserveSig]
        int GetDisplayName([MarshalAs(UnmanagedType.LPWStr)] out string displayName);

        [PreserveSig]
        int SetDisplayName([MarshalAs(UnmanagedType.LPWStr)] string value,
            [MarshalAs(UnmanagedType.LPStruct)] Guid eventContext);

        [PreserveSig]
        int GetIconPath([MarshalAs(UnmanagedType.LPWStr)] out string iconPath);

        [PreserveSig]
        int SetIconPath([MarshalAs(UnmanagedType.LPWStr)] string value,
            [MarshalAs(UnmanagedType.LPStruct)] Guid eventContext);

        [PreserveSig]
        int GetGroupingParam(out Guid groupingId);

        [PreserveSig]
        int SetGroupingParam([MarshalAs(UnmanagedType.LPStruct)] Guid groupingId,
            [MarshalAs(UnmanagedType.LPStruct)] Guid eventContext);

        [PreserveSig]
        int RegisterAudioSessionNotification(IntPtr client);

        [PreserveSig]
        int UnregisterAudioSessionNotification(IntPtr client);

        [PreserveSig]
        int GetSessionIdentifier([MarshalAs(UnmanagedType.LPWStr)] out string sessionIdentifier);

        [PreserveSig]
        int GetSessionInstanceIdentifier([MarshalAs(UnmanagedType.LPWStr)] out string sessionInstanceIdentifier);

        [PreserveSig]
        int GetProcessId(out uint processId);

        [PreserveSig]
        int IsSystemSoundsSession();

        [PreserveSig]
        int SetDuckingPreference([MarshalAs(UnmanagedType.Bool)] bool optOut);
    }

    [ComImport, Guid("87CE5498-68D6-44E5-9215-6DA47EF883D8"),
     InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]
    internal interface ISimpleAudioVolume {
        [PreserveSig]
        int SetMasterVolume(float level, ref Guid eventContext);

        [PreserveSig]
        int GetMasterVolume(out float level);

        [PreserveSig]
        int SetMute([MarshalAs(UnmanagedType.Bool)] bool mute, ref Guid eventContext);

        [PreserveSig]
        int GetMute([MarshalAs(UnmanagedType.Bool)] out bool mute);
    }

    [DllImport("kernel32.dll", SetLastError = true)]
    private static extern IntPtr OpenProcess(uint desiredAccess, bool inheritHandle, uint processId);

    [DllImport("kernel32.dll", SetLastError = true)]
    [return: MarshalAs(UnmanagedType.Bool)]
    private static extern bool CloseHandle(IntPtr handle);

    [DllImport("kernel32.dll", CharSet = CharSet.Unicode)]
    private static extern int GetApplicationUserModelId(
        IntPtr processHandle, ref uint applicationUserModelIdLength,
        IntPtr applicationUserModelId);

    [DllImport("kernel32.dll", CharSet = CharSet.Unicode,
        EntryPoint = "GetApplicationUserModelId")]
    private static extern int GetApplicationUserModelIdBuffer(
        IntPtr processHandle, ref uint applicationUserModelIdLength,
        StringBuilder applicationUserModelId);

    [DllImport("kernel32.dll", CharSet = CharSet.Unicode, SetLastError = true)]
    [return: MarshalAs(UnmanagedType.Bool)]
    private static extern bool QueryFullProcessImageName(
        IntPtr processHandle, int flags, StringBuilder executableName, ref int size);

    private static void Release(object value) {
        if (value != null && Marshal.IsComObject(value)) {
            try { Marshal.ReleaseComObject(value); } catch { }
        }
    }

    private static IAudioSessionManager2 OpenManager(
        out IMMDeviceEnumerator deviceEnumerator, out IMMDevice device) {

        deviceEnumerator = (IMMDeviceEnumerator)(new MMDeviceEnumerator());
        device = null;

        int hr = deviceEnumerator.GetDefaultAudioEndpoint(E_RENDER, E_MULTIMEDIA, out device);
        Marshal.ThrowExceptionForHR(hr);

        object result;
        Guid iid = typeof(IAudioSessionManager2).GUID;
        hr = device.Activate(iid, CLSCTX_ALL, IntPtr.Zero, out result);
        Marshal.ThrowExceptionForHR(hr);
        return (IAudioSessionManager2)result;
    }

    private static string Normalize(string value) {
        return String.IsNullOrWhiteSpace(value)
            ? String.Empty
            : value.Trim().ToLowerInvariant().Replace('\\', '/');
    }

    private static string AppLeaf(string value) {
        string normalized = Normalize(value);
        int bang = normalized.LastIndexOf('!');
        if (bang >= 0 && bang + 1 < normalized.Length) {
            normalized = normalized.Substring(bang + 1);
        }

        int slash = normalized.LastIndexOf('/');
        if (slash >= 0 && slash + 1 < normalized.Length) {
            normalized = normalized.Substring(slash + 1);
        }

        if (normalized.EndsWith(".exe", StringComparison.OrdinalIgnoreCase)) {
            normalized = normalized.Substring(0, normalized.Length - 4);
        }
        return normalized;
    }

    private static string GetProcessAppId(IntPtr processHandle) {
        uint length = 0;
        GetApplicationUserModelId(processHandle, ref length, IntPtr.Zero);
        if (length == 0 || length > 4096) return String.Empty;

        StringBuilder buffer = new StringBuilder((int)length);
        int result = GetApplicationUserModelIdBuffer(processHandle, ref length, buffer);
        return result == 0 ? buffer.ToString() : String.Empty;
    }

    private static string GetProcessName(uint processId, IntPtr processHandle) {
        try {
            StringBuilder path = new StringBuilder(1024);
            int length = path.Capacity;
            if (QueryFullProcessImageName(processHandle, 0, path, ref length)) {
                return Path.GetFileNameWithoutExtension(path.ToString());
            }
        } catch { }

        try {
            return Process.GetProcessById((int)processId).ProcessName;
        } catch {
            return String.Empty;
        }
    }

    private static int MatchScore(string sourceAppId, uint processId) {
        string source = Normalize(sourceAppId);
        if (source.Length == 0 || processId == 0) return 0;

        IntPtr processHandle = OpenProcess(PROCESS_QUERY_LIMITED_INFORMATION, false, processId);
        if (processHandle == IntPtr.Zero) return 0;

        try {
            string processAppId = Normalize(GetProcessAppId(processHandle));
            if (processAppId.Length > 0 && source == processAppId) return 100;

            string sourceLeaf = AppLeaf(source);
            string processName = AppLeaf(GetProcessName(processId, processHandle));
            if (sourceLeaf.Length > 0 && sourceLeaf == processName) return 90;

            if (processName.Length >= 4 && source.Contains(processName)) return 70;
            if (sourceLeaf.Length >= 4 && processAppId.Contains(sourceLeaf)) return 70;

            return 0;
        } finally {
            CloseHandle(processHandle);
        }
    }

    public static bool IsAvailable() {
        IMMDeviceEnumerator deviceEnumerator = null;
        IMMDevice device = null;
        IAudioSessionManager2 manager = null;
        IAudioSessionEnumerator sessions = null;

        try {
            manager = OpenManager(out deviceEnumerator, out device);
            Marshal.ThrowExceptionForHR(manager.GetSessionEnumerator(out sessions));
            int count;
            Marshal.ThrowExceptionForHR(sessions.GetCount(out count));
            return count >= 0;
        } finally {
            Release(sessions);
            Release(manager);
            Release(device);
            Release(deviceEnumerator);
        }
    }

    public static int Get(string sourceAppId) {
        if (String.IsNullOrWhiteSpace(sourceAppId)) return -1;

        IMMDeviceEnumerator deviceEnumerator = null;
        IMMDevice device = null;
        IAudioSessionManager2 manager = null;
        IAudioSessionEnumerator sessions = null;

        int bestScore = 0;
        int bestState = -1;
        int bestVolume = -1;

        try {
            manager = OpenManager(out deviceEnumerator, out device);
            Marshal.ThrowExceptionForHR(manager.GetSessionEnumerator(out sessions));

            int count;
            Marshal.ThrowExceptionForHR(sessions.GetCount(out count));

            for (int index = 0; index < count; index++) {
                IAudioSessionControl2 control = null;
                try {
                    if (sessions.GetSession(index, out control) < 0 || control == null) continue;

                    ISimpleAudioVolume volume = control as ISimpleAudioVolume;
                    if (volume == null) continue;

                    uint processId;
                    if (control.GetProcessId(out processId) < 0) continue;

                    AudioSessionState state;
                    if (control.GetState(out state) < 0 || state == AudioSessionState.Expired) continue;

                    int score = MatchScore(sourceAppId, processId);
                    if (score <= 0) continue;

                    float level;
                    if (volume.GetMasterVolume(out level) < 0) continue;

                    int stateScore = state == AudioSessionState.Active ? 1 : 0;
                    if (score > bestScore || (score == bestScore && stateScore > bestState)) {
                        bestScore = score;
                        bestState = stateScore;
                        bestVolume = (int)Math.Round(Math.Max(0.0f, Math.Min(1.0f, level)) * 100.0f);
                    }
                } finally {
                    Release(control);
                }
            }

            return bestVolume;
        } finally {
            Release(sessions);
            Release(manager);
            Release(device);
            Release(deviceEnumerator);
        }
    }

    public static bool Set(string sourceAppId, int percent) {
        if (String.IsNullOrWhiteSpace(sourceAppId)) return false;

        float level = Math.Max(0, Math.Min(100, percent)) / 100.0f;
        Guid eventContext = Guid.Empty;

        IMMDeviceEnumerator deviceEnumerator = null;
        IMMDevice device = null;
        IAudioSessionManager2 manager = null;
        IAudioSessionEnumerator sessions = null;
        int bestScore = 0;
        int bestState = -1;
        int bestIndex = -1;
        uint bestProcessId = 0;

        try {
            manager = OpenManager(out deviceEnumerator, out device);
            Marshal.ThrowExceptionForHR(manager.GetSessionEnumerator(out sessions));

            int count;
            Marshal.ThrowExceptionForHR(sessions.GetCount(out count));

            for (int index = 0; index < count; index++) {
                IAudioSessionControl2 control = null;
                try {
                    if (sessions.GetSession(index, out control) < 0 || control == null) continue;

                    ISimpleAudioVolume candidateVolume = control as ISimpleAudioVolume;
                    if (candidateVolume == null) continue;

                    uint processId;
                    if (control.GetProcessId(out processId) < 0) continue;

                    AudioSessionState state;
                    if (control.GetState(out state) < 0 || state == AudioSessionState.Expired) continue;

                    int score = MatchScore(sourceAppId, processId);
                    if (score <= 0) continue;

                    int stateScore = state == AudioSessionState.Active ? 1 : 0;
                    if (score > bestScore || (score == bestScore && stateScore > bestState)) {
                        bestScore = score;
                        bestState = stateScore;
                        bestIndex = index;
                        bestProcessId = processId;
                    }
                } finally {
                    Release(control);
                }
            }

            if (bestIndex < 0) return false;

            IAudioSessionControl2 selected = null;
            try {
                if (sessions.GetSession(bestIndex, out selected) < 0 || selected == null) return false;

                uint selectedProcessId;
                if (selected.GetProcessId(out selectedProcessId) < 0 ||
                    selectedProcessId != bestProcessId) return false;

                ISimpleAudioVolume selectedVolume = selected as ISimpleAudioVolume;
                return selectedVolume != null &&
                    selectedVolume.SetMasterVolume(level, ref eventContext) >= 0;
            } finally {
                Release(selected);
            }
        } finally {
            Release(sessions);
            Release(manager);
            Release(device);
            Release(deviceEnumerator);
        }
    }
}
'@
    }
    $audioReady = [RoseAudio]::IsAvailable()
} catch {
    $audioReady = $false
}

function Get-Volume([string] $sourceAppId) {
    if (-not $audioReady -or [string]::IsNullOrWhiteSpace($sourceAppId)) { return -1 }
    try { return [RoseAudio]::Get($sourceAppId) } catch { return -1 }
}


function Save-Thumbnail($properties, $path) {
    if ($null -eq $properties.Thumbnail) { return $false }

    $winrtStream = Wait-Async ($properties.Thumbnail.OpenReadAsync()) ([Windows.Storage.Streams.IRandomAccessStreamWithContentType])
    $netStream = $asStreamMethod.Invoke($null, @($winrtStream))
    try {
        $buffer = New-Object System.IO.MemoryStream
        try {
            $netStream.CopyTo($buffer)
            $bytes = $buffer.ToArray()
        } finally {
            $buffer.Dispose()
        }

        if ($bytes.Length -le 0) { return $false }

        $temporary = "$path.tmp"
        [System.IO.File]::WriteAllBytes($temporary, $bytes)
        Move-Item -LiteralPath $temporary -Destination $path -Force
        return $true
    } finally {
        $netStream.Dispose()
    }
}


function Invoke-MediaAction([string] $name, [long] $value) {
    $session = $manager.GetCurrentSession()
    if ($null -eq $session) { return }

    if ($name -eq 'volume') {
        if ($audioReady) {
            $null = [RoseAudio]::Set([string] $session.SourceAppUserModelId, [int] $value)
        }
        return
    }

    switch ($name) {
        'play' { $null = Wait-Async ($session.TryPlayAsync()) ([bool]) }
        'pause' { $null = Wait-Async ($session.TryPauseAsync()) ([bool]) }
        'toggle' { $null = Wait-Async ($session.TryTogglePlayPauseAsync()) ([bool]) }
        'next' { $null = Wait-Async ($session.TrySkipNextAsync()) ([bool]) }
        'previous' { $null = Wait-Async ($session.TrySkipPreviousAsync()) ([bool]) }
        'seek' { $null = Wait-Async ($session.TryChangePlaybackPositionAsync([long] ($value * 10000))) ([bool]) }
    }
}

if ($Action -ne 'watch') {
    Invoke-MediaAction $Action $Value
    exit 0
}


$stdout = New-Object System.IO.StreamWriter([Console]::OpenStandardOutput(), (New-Object System.Text.UTF8Encoding($false)))
$stdout.AutoFlush = $true

function Write-State($state) {
    $stdout.WriteLine(($state | ConvertTo-Json -Compress))
}

function Read-Commands {
    if (-not (Test-Path -LiteralPath $CommandPath)) { return }

    try {
        $lines = @(Get-Content -LiteralPath $CommandPath -Encoding UTF8)
        Remove-Item -LiteralPath $CommandPath -Force
    } catch {
        return
    }

    foreach ($line in $lines) {
        if ([string]::IsNullOrWhiteSpace($line)) { continue }

        $parts = $line.Trim().Split(' ')
        $value = 0
        if ($parts.Count -gt 1) { [void][long]::TryParse($parts[1], [ref] $value) }

        try { Invoke-MediaAction $parts[0] $value } catch { }
    }
}

function Publish-State {
    $session = $manager.GetCurrentSession()

    if ($null -eq $session) {
        $script:lastThumbId = ''
        Write-State ([pscustomobject] @{ ok = $false; reason = 'no-session'; volume = -1 })
        return
    }

    $properties = Wait-Async ($session.TryGetMediaPropertiesAsync()) ([Windows.Media.Control.GlobalSystemMediaTransportControlsSessionMediaProperties])
    $timeline = $session.GetTimelineProperties()
    $playback = $session.GetPlaybackInfo()

    $title = [string] $properties.Title
    $artist = [string] $properties.Artist
    if ([string]::IsNullOrWhiteSpace($artist)) { $artist = [string] $properties.AlbumArtist }

    $thumbId = "$title|$artist".GetHashCode().ToString()
    if ($thumbId -ne $script:lastThumbId) {
        $saved = $false
        try { $saved = Save-Thumbnail $properties $ArtPath } catch { $saved = $false }
        $script:lastThumbId = if ($saved) { $thumbId } else { 'none' }
    }

    $age = 0
    if ($timeline.LastUpdatedTime.Year -gt 1601) {
        $age = [long] ([DateTimeOffset]::Now - $timeline.LastUpdatedTime).TotalMilliseconds
        if ($age -lt 0) { $age = 0 }
    }

    Write-State ([pscustomobject] @{
            ok       = $true
            title    = $title
            artist   = $artist
            app      = [string] $session.SourceAppUserModelId
            status   = [string] $playback.PlaybackStatus
            position = [long] $timeline.Position.TotalMilliseconds
            duration = [long] ($timeline.EndTime.TotalMilliseconds - $timeline.StartTime.TotalMilliseconds)
            age      = $age
            canSeek  = [bool] $playback.Controls.IsPlaybackPositionEnabled
            canNext  = [bool] $playback.Controls.IsNextEnabled
            canPrev  = [bool] $playback.Controls.IsPreviousEnabled
            thumb    = if ($script:lastThumbId -eq 'none') { '' } else { $ArtPath }
            thumbId  = $script:lastThumbId
            volume   = (Get-Volume ([string] $session.SourceAppUserModelId))
        })
}

$lastThumbId = ''
$commandChecks = 8
$sleepMs = [Math]::Max(50, [int] ($IntervalMs / $commandChecks))

while ($true) {
    try {
        Publish-State
    } catch {
        Write-State ([pscustomobject] @{ ok = $false; reason = [string] $_.Exception.Message })
    }

    for ($check = 0; $check -lt $commandChecks; $check++) {
        Start-Sleep -Milliseconds $sleepMs
        Read-Commands
    }
}
