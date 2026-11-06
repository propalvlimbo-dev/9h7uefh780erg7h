package sg.gui.manager.now;

import java.util.Random;

public class NicknameGenerator {
    private static final String[] PREFIXES = {
        "Vortex", "Shadow", "Ghost", "Frost", "Nova", "Zenith", "Blaze", "Apex",
        "Echo", "Krypton", "Phantom", "Pulse", "Cyber", "Pixel", "Neon", "Titan",
        "Aero", "Delta", "Nexus", "Chaos", "Drift", "Flux", "Omega", "Solar"
    };
    private static final Random RANDOM = new Random();

    public String formatText11() {
        String prefix = PREFIXES[RANDOM.nextInt(PREFIXES.length)];
        int num = RANDOM.nextInt(900) + 100;
        return prefix + "_" + num;
    }
}
