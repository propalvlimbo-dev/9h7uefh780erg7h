import math
from PIL import Image

ART = Image.open('src/main/resources/assets/rose/textures/wings/rose_wings.png').convert('RGBA')
TEX = 512.0

def rot_y(v, a):
    c, s = math.cos(a), math.sin(a)
    return (v[0]*c + v[2]*s, v[1], -v[0]*s + v[2]*c)

def rot_x(v, a):
    c, s = math.cos(a), math.sin(a)
    return (v[0], v[1]*c - v[2]*s, v[1]*s + v[2]*c)

PIVOT = (0.0, 4.0, 2.5)
def wing_corners(sweep_deg, tilt_deg, mirror):
    # локальные углы плоскости: (1,6) (27,6) (27,-22) (1,-22)
    local = [(1, 6), (27, 6), (27, -22), (1, -22)]
    pts = []
    for (x, y) in local:
        px, py, pz = x - PIVOT[0], y - PIVOT[1], -PIVOT[2]
        sgn = -1.0 if mirror else 1.0
        # наклон (Z)
        az = math.radians(tilt_deg) * sgn
        px, py = px*math.cos(az) - py*math.sin(az), px*math.sin(az) + py*math.cos(az)
        # разворот назад (Y)
        ay = math.radians(sweep_deg) * sgn
        px, pz = px*math.cos(ay) + pz*math.sin(ay), -px*math.sin(ay) + pz*math.cos(ay)
        pts.append((px + PIVOT[0], py + PIVOT[1], pz + PIVOT[2]))
    return pts

def render(yaw_deg, pitch_deg, flap=25.0, path='/tmp/x.png', size=760, scale=15.0, center=(0.0, -4.0, 0.0)):
    img = Image.new('RGBA', (size, size), (18, 14, 20, 255))
    wings = []
    for mirror in (False, True):
        corners = wing_corners(42 - flap, 4.0, mirror)
        a, p = math.radians(yaw_deg), math.radians(pitch_deg)
        pts = [rot_x(rot_y(v, a), p) for v in corners]
        screen = [(size/2 + (v[0]-center[0])*scale, size/2 - (v[1]-center[1])*scale) for v in pts]
        depth = sum(v[2] for v in pts)/4
        wings.append((depth, screen, mirror))
    wings.sort(key=lambda t: t[0])
    for _, screen, mirror in wings:
        # screen[0] = (u=0,v=0) корень-верх, [1] = (1,0), [2]=(1,1), [3]=(0,1)
        O = screen[0]
        Ax, Ay = screen[1][0]-O[0], screen[1][1]-O[1]
        Bx, By = screen[3][0]-O[0], screen[3][1]-O[1]
        det = Ax*By - Bx*Ay
        if abs(det) < 1e-6:
            continue
        # инверсия: S = O + u*A + v*B  =>  [u,v] = M^-1 (S-O)
        a11, a12 = By/det, -Bx/det
        a21, a22 = -Ay/det, Ax/det
        coeffs = (a11*TEX, a12*TEX, (-a11*O[0]-a12*O[1])*TEX,
                  a21*TEX, a22*TEX, (-a21*O[0]-a22*O[1])*TEX)
        warped = ART.transform((size, size), Image.AFFINE, coeffs, resample=Image.BICUBIC)
        if mirror:
            warped = warped.transpose(Image.FLIP_LEFT_RIGHT)
        img.alpha_composite(warped)
    img.convert('RGB').save(path)

tiles = []
for yaw, pitch in ((180, 0), (215, 10), (145, 8), (255, 18)):
    p = '/tmp/_pw_%d.png' % yaw
    render(yaw, pitch, path=p)
    tiles.append(Image.open(p))
sheet = Image.new('RGB', (1520, 1520))
for i, t in enumerate(tiles):
    sheet.paste(t, ((i % 2)*760, (i//2)*760))
sheet.save('/tmp/planar_sheet.png')
print('ok')
