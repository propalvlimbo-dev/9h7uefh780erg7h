"""
Оффлайн-превью косметики «Роза»: форма и UV-развёртка как в клиенте, цвет — по настоящей текстуре.
Нужен, чтобы видеть силуэт до запуска игры (painter's algorithm, без numpy).

  python3 tools/preview/render_cosmetic.py [--out file.png] [--time 0.0] [--scale 17]
"""
import math, os, sys
from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
TEX = os.path.join(ROOT, 'src/main/resources/assets/rose/textures/wings/rose_wings.png')
TEX_W = TEX_H = 64
SIZE = 760
BG = (18, 14, 20)


class Part:
    def __init__(self, pivot=(0.0, 0.0, 0.0)):
        self.pivot = pivot
        self.quads = []
        self.children = []


def add_box(part, x, y, z, w, h, d, tu, tv, sign=1):
    """UV-развёртка ровно как WingModelPart.addCuboid."""
    X0 = x if sign > 0 else -(x + w); X1 = X0 + w
    Y0, Y1, Z0, Z1 = y, y + h, z, z + d
    uW, uF, uE = tu, tu + d, tu + d + w
    uT, uB, uBE = tu + d + 2 * w, tu + 2 * d + w, tu + 2 * d + 2 * w
    vT, vS, vSE = tv, tv + d, tv + d + h
    faces = [
        ([(X0, Y1, Z0), (X1, Y1, Z0), (X1, Y1, Z1), (X0, Y1, Z1)], (0, -1, 0), (uF, vT, uE, vS)),
        ([(X1, Y0, Z1), (X0, Y0, Z1), (X0, Y0, Z0), (X1, Y0, Z0)], (0, 1, 0), (uE, vS, uT, vT)),
        ([(X0, Y0, Z0), (X0, Y0, Z1), (X0, Y1, Z1), (X0, Y1, Z0)], (-1, 0, 0), (uW, vS, uF, vSE)),
        ([(X1, Y0, Z0), (X0, Y0, Z0), (X0, Y1, Z0), (X1, Y1, Z0)], (0, 0, -1), (uF, vS, uE, vSE)),
        ([(X1, Y0, Z1), (X1, Y0, Z0), (X1, Y1, Z0), (X1, Y1, Z1)], (1, 0, 0), (uE, vS, uB, vSE)),
        ([(X0, Y0, Z1), (X1, Y0, Z1), (X1, Y1, Z1), (X0, Y1, Z1)], (0, 0, 1), (uB, vS, uBE, vSE)),
    ]
    for pos, nrm, (u0, v0, u1, v1) in faces:
        uvs = [(u0, v1), (u1, v1), (u1, v0), (u0, v0)]
        part.quads.append((pos, nrm, [(a / TEX_W, b / TEX_H) for a, b in uvs]))


def add_plate(part, x, y, z, w, h, tu, tv, sign=1):
    X0 = x if sign > 0 else -(x + w); X1 = X0 + w
    uv = [(tu / TEX_W, tv / TEX_H), ((tu + w) / TEX_W, tv / TEX_H),
          ((tu + w) / TEX_W, (tv + h) / TEX_H), (tu / TEX_W, (tv + h) / TEX_H)]
    part.quads.append(([(X0, y, z), (X1, y, z), (X1, y + h, z), (X0, y + h, z)], (0, 0, -1), uv))
    part.quads.append(([(X1, y, z + 1), (X0, y, z + 1), (X0, y + h, z + 1), (X1, y + h, z + 1)],
                       (0, 0, 1), [uv[1], uv[0], uv[3], uv[2]]))


def strip_plate(parent, x, y, z, w, h, tu, tv, pivot, sign):
    strips = max(2, w // 2)
    step = w / strips
    for i in range(strips):
        pos = i / max(1, strips - 1)
        wave = math.sin((i + 1) * 2.37 + w * 0.9)
        taper = 1.0 - 0.42 * abs(pos - 0.42) * 2.0
        jitter = 0.62 + 0.38 * (0.5 + 0.5 * wave)
        sh = max(2, round(h * taper * jitter))
        drop = 0.0 if i % 2 == 0 else -1.0
        p = Part(pivot)
        add_plate(p, x + i * step, y + drop, z + 0.5 * (i % 2), max(1, int(step)), sh, tu, tv, sign)
        parent.children.append(p)


MATS = {'wood': (0, 0), 'wood_light': (48, 16), 'thorn': (16, 0), 'leaf': (32, 0),
        'leaf_dark': (48, 0), 'rose_core': (0, 16), 'rose_petal': (16, 16), 'rose_hi': (32, 16)}


def build_rose(sign):
    """Тот же спек, что в sg.ec.RoseWingModel: ствол + семь боковых побегов с розами."""
    root = Part()
    mats = {m: Part() for m in MATS}
    for part in mats.values():
        root.children.append(part)

    def add(mat, x, y, z, w=1, h=1, d=1):
        tu, tv = MATS[mat]
        add_box(mats[mat], x, y, z, w, h, d, tu, tv, sign)

    def rose(cx, cy, cz):
        add('rose_core', cx, cy, cz)
        add('rose_petal', cx - 1, cy, cz)
        add('rose_petal', cx + 1, cy, cz)
        add('rose_petal', cx, cy - 1, cz)
        add('rose_petal', cx, cy + 1, cz)
        add('rose_hi', cx, cy, cz + 1)

    stem_x = 1.0
    y = -14.0
    first = True
    while y <= 5.0:
        thick = 2 if first else 1
        add('wood', stem_x, y, 0, thick, 1, thick)
        if int(y) % 4 == 0:
            add('wood_light', stem_x, y, 1)
        first = False
        y += 1.2

    for index, (sy, ang, length) in enumerate([(-13.0, 80, 10.0), (-10.0, 64, 13.0), (-7.0, 48, 15.0), (-4.0, 32, 17.0),
                            (-1.0, 16, 16.0), (2.0, 2, 13.0), (4.5, -14, 9.0)]):
        a = math.radians(ang)
        dx, dy = math.sin(a), -math.cos(a)
        steps = int(length / 1.6)
        for st in range(1, steps):
            px, py = stem_x + 1.0 + dx * 1.6 * st, sy + dy * 1.6 * st
            add('wood', px, py, 0)
            if st in (1, 4, 7) and st < steps - 1:
                add('thorn', px, py - 1, 0)
            if st == steps - 2:
                add('leaf', px + 1, py, 1, 2, 1, 1)
        if index % 2 == 1:
            rose(stem_x + 1.0 + dx * 1.6 * (steps - 1), sy + dy * 1.6 * (steps - 1), 1)
    return root


def flatten(part, out=None):
    """Без вращений: пивоты геометрию не сдвигают (как ModelPart.render при нулевом повороте)."""
    if out is None:
        out = []
    out.extend(part.quads)
    for ch in part.children:
        flatten(ch, out)
    return out


def rotate(yaw, pitch):
    yaw, pitch = math.radians(yaw), math.radians(pitch)
    cy, sy, cp, sp = math.cos(yaw), math.sin(yaw), math.cos(pitch), math.sin(pitch)
    return lambda p: (
        p[0] * cy + p[2] * sy,
        (p[1] * cp - (-p[0] * sy + p[2] * cy) * sp),
        p[1] * sp + (-p[0] * sy + p[2] * cy) * cp,
    )


def quad_color(tex, uvs, nrm, view_light, time):
    us = [uv[0] for uv in uvs]; vs = [uv[1] for uv in uvs]
    u = min(max(sum(us) / 4, 0), 0.999); v = min(max(sum(vs) / 4, 0), 0.999)
    r, g, b, a = tex.getpixel((int(u * TEX_W), int(v * TEX_H)))
    if a < 8:
        return None
    shade = 0.62 + 0.38 * max(0.0, nrm[0] * view_light[0] + nrm[1] * view_light[1] + nrm[2] * view_light[2])
    rose = u > 0.690
    leaf = 0.405 <= u <= 0.690
    pulse = 0.62 + 0.38 * math.sin(time * 1.9 + v * 6.0)
    glow = (0.80 if rose else 0.20 if leaf else 0.0) * pulse
    r = min(255, r * shade + 200 * glow)
    g = min(255, g * shade + 70 * glow)
    b = min(255, b * shade + 95 * glow)
    return (int(r), int(g), int(b))


def render(quads, yaw, pitch, center, scale, time, out):
    rot = rotate(yaw, pitch)
    light = (0.34, -0.86, -0.38)
    tex = Image.open(TEX).convert('RGBA')
    img = Image.new('RGB', (SIZE, SIZE), BG)
    draw = ImageDraw.Draw(img)
    items = []
    for pos, nrm, uvs in quads:
        rp = [rot((p[0] - 0, p[1], p[2])) for p in pos]
        rn = rot(nrm)
        if rn[2] > 0.02:          # задние грани пропускаем
            continue
        poly = [(SIZE / 2 + (p[0] - center[0]) * scale, SIZE / 2 + (p[1] - center[1]) * scale) for p in rp]
        depth = sum(p[2] for p in rp) / 4
        col = quad_color(tex, uvs, rn, light, time)
        if col is None:
            continue
        items.append((depth, poly, col))
    items.sort(key=lambda t: t[0], reverse=True)
    for _, poly, col in items:
        draw.polygon(poly, fill=col, outline=tuple(int(c * 0.82) for c in col))
    img.save(out)


if __name__ == '__main__':
    args = sys.argv
    out = args[args.index('--out') + 1] if '--out' in args else os.path.join(ROOT, 'assets/branding/rose-wings-render.png')
    time = float(args[args.index('--time') + 1]) if '--time' in args else 0.0
    scale = float(args[args.index('--scale') + 1]) if '--scale' in args else 15.0

    quads = flatten(build_rose(1)) + flatten(build_rose(-1))
    tiles = []
    for yaw, pitch in ((205, 10), (335, 6), (250, 26), (120, 4)):
        path = '/tmp/_v_%d_%d.png' % (yaw, pitch)
        render(quads, yaw, pitch, (5.5, -3.0, 0.0), scale, time, path)
        tiles.append(Image.open(path))
    sheet = Image.new('RGB', (SIZE * 2, SIZE * 2), BG)
    for i, tile in enumerate(tiles):
        sheet.paste(tile, ((i % 2) * SIZE, (i // 2) * SIZE))
    sheet.save(out)
    print('rendered', out)
