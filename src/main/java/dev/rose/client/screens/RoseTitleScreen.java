package dev.rose.client.screens;

import dev.rose.Rose;
import dev.rose.client.render.RoseDraw;
import dev.rose.client.render.RoseTheme;
import dev.rose.client.ui.Anim;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * The Rose main menu: fully custom, animated, glass + ruby.
 */
public class RoseTitleScreen extends Screen {
	private static final Identifier LOGO = Identifier.fromNamespaceAndPath("rose", "textures/gui/logo_192.png");

	private static final int LOGO_TEX = 192;
	private static final float LOGO_SCALE = 0.62F;
	private static final float LOGO_DRAW = LOGO_TEX * LOGO_SCALE;

	private static final String[] BUTTON_KEYS = {
		"rose.title.singleplayer",
		"rose.title.multiplayer",
		"rose.title.settings",
		"rose.title.client",
		"rose.title.quit"
	};

	private static final float BUTTON_W = 216.0F;
	private static final float BUTTON_H = 24.0F;
	private static final float BUTTON_GAP = 6.0F;

	private final float[] hover = new float[BUTTON_KEYS.length];
	private final float[] dust = new float[64 * 5];

	private float startTime;
	private float lastTime;
	private float dt;
	private int hovered = -1;

	public RoseTitleScreen() {
		super(Component.literal("Rose"));
	}

	@Override
	protected void init() {
		super.init();

		float now = Anim.now();
		if (startTime <= 0.0F) {
			startTime = now;
		}
		lastTime = now;

		java.util.Random random = new java.util.Random(0x5EED);

		for (int i = 0; i < dust.length / 5; i++) {
			dust[i * 5] = random.nextFloat();
			dust[i * 5 + 1] = random.nextFloat();
			dust[i * 5 + 2] = 0.6F + random.nextFloat() * 1.6F;
			dust[i * 5 + 3] = 0.006F + random.nextFloat() * 0.02F;
			dust[i * 5 + 4] = random.nextFloat() * 6.2832F;
		}
	}

	// ------------------------------------------------------------- layout

	private float blockTop() {
		float blockH = LOGO_DRAW + 56.0F + BUTTON_KEYS.length * (BUTTON_H + BUTTON_GAP);
		return Math.max(10.0F, (this.height - blockH) * 0.5F - 6.0F);
	}

	private float buttonY(int index) {
		return blockTop() + LOGO_DRAW + 52.0F + index * (BUTTON_H + BUTTON_GAP);
	}

	private float buttonX() {
		return (this.width - BUTTON_W) * 0.5F;
	}

	private int hitTest(double mouseX, double mouseY) {
		float bx = buttonX();

		for (int i = 0; i < BUTTON_KEYS.length; i++) {
			float by = buttonY(i);

			if (mouseX >= bx - 4 && mouseX <= bx + BUTTON_W + 4 && mouseY >= by && mouseY <= by + BUTTON_H) {
				return i;
			}
		}

		return -1;
	}

	// ------------------------------------------------------------ render

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		float now = Anim.now();
		dt = Math.min(0.1F, Math.max(0.0F, now - lastTime));
		lastTime = now;

		hovered = hitTest(mouseX, mouseY);

		for (int i = 0; i < hover.length; i++) {
			hover[i] = Anim.approach(hover[i], i == hovered ? 1.0F : 0.0F, 16.0F, dt);
		}

		renderBackground(g, mouseX, mouseY, partialTick);
		renderLogo(g, now);
		renderButtons(g, now);
		renderFooter(g);
	}

	@Override
	public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		float now = Anim.now();

		// base gradient
		RoseDraw.gradientV(g, 0, 0, this.width, this.height, 0xFF150710, 0xFF050308);

		// slow drifting ruby nebulas
		for (int i = 0; i < 3; i++) {
			float phase = i * 2.1F;
			float cx = this.width * (0.5F + 0.32F * (float) Math.sin(now * 0.11F + phase));
			float cy = this.height * (0.34F + 0.18F * (float) Math.cos(now * 0.085F + phase * 1.7F));
			float radius = Math.min(this.width, this.height) * (0.34F + 0.06F * i);

			RoseDraw.glow(g, cx, cy, radius, 0x22E4294B, 16);
		}

		// faint vertical aurora curtains
		for (int i = 0; i < 4; i++) {
			float t = (i + 0.5F) / 4.0F;
			float cx = this.width * t + (float) Math.sin(now * 0.2F + i) * 26.0F;
			float w = this.width * 0.09F;
			int a = (int) (10.0F + 8.0F * (float) Math.sin(now * 0.35F + i * 1.3F));

			RoseDraw.gradientH(g, cx - w, 0, w * 2.0F, this.height,
				RoseTheme.withAlpha(RoseTheme.RUBY, 0), RoseTheme.withAlpha(RoseTheme.RUBY, Math.max(0, a)));
		}

		// dust
		for (int i = 0; i < dust.length / 5; i++) {
			float px = dust[i * 5];
			float py = dust[i * 5 + 1];
			float size = dust[i * 5 + 2];
			float speed = dust[i * 5 + 3];
			float phase = dust[i * 5 + 4];

			float y = (py - now * speed) % 1.0F;
			if (y < 0.0F) {
				y += 1.0F;
			}

			float x = px + (float) Math.sin(now * 0.25F + phase) * 0.01F;
			int alpha = (int) (26.0F + 40.0F * (0.5F + 0.5F * (float) Math.sin(now * 1.2F + phase)));

			RoseDraw.circle(g, x * this.width, y * this.height, size,
				RoseTheme.withAlpha(0xFFFFD9E1, alpha));
		}

		// vignette
		RoseDraw.gradientV(g, 0, 0, this.width, this.height * 0.32F, 0x66000000, 0x00000000);
		RoseDraw.gradientV(g, 0, this.height * 0.6F, this.width, this.height * 0.4F, 0x00000000, 0xAA000000);
		RoseDraw.gradientH(g, 0, 0, this.width * 0.22F, this.height, 0x55000000, 0x00000000);
		RoseDraw.gradientH(g, this.width * 0.78F, 0, this.width * 0.22F, this.height, 0x00000000, 0x55000000);
	}

	private void renderLogo(GuiGraphics g, float now) {
		float p = Anim.easeOutQuint(Anim.progress(startTime, 0.9F, now));
		float cx = this.width * 0.5F;
		float bob = (float) Math.sin(now * 1.05F) * 2.5F;
		float scale = LOGO_SCALE * (0.88F + 0.12F * p);
		float logoCenterY = blockTop() + LOGO_DRAW * 0.5F + bob;

		// soft halo behind the rose
		RoseDraw.glow(g, cx, logoCenterY, LOGO_DRAW * 0.62F,
			RoseTheme.withAlpha(RoseTheme.RUBY, (int) (52.0F * p)), 14);

		int tint = RoseTheme.withAlpha(0xFFFFFFFF, (int) (255.0F * p));

		g.pose().pushMatrix();
		g.pose().translate(cx, logoCenterY);
		g.pose().scale(scale);
		g.pose().translate(-LOGO_TEX * 0.5F, -LOGO_TEX * 0.5F);
		RoseDraw.image(g, LOGO, 0, 0, LOGO_TEX, tint);
		g.pose().popMatrix();

		// wordmark
		float wp = Anim.easeOutCubic(Anim.progress(startTime + 0.15F, 0.7F, now));
		float wy = blockTop() + LOGO_DRAW + 4.0F;
		float ww = RoseDraw.textSpacedWidth("ROSE", 2.0F, 2.0F);
		int walpha = (int) (255.0F * wp);

		if (walpha > 4) {
			for (int i = 0; i < 6; i++) {
				float ang = (float) (Math.PI * 2.0 * i / 6.0);
				RoseDraw.textSpaced(g, "ROSE", cx - ww * 0.5F + (float) Math.cos(ang) * 2.0F, wy + (float) Math.sin(ang) * 2.0F,
					2.0F, 2.0F, RoseTheme.withAlpha(RoseTheme.RUBY, walpha / 6), false);
			}
			RoseDraw.textSpaced(g, "ROSE", cx - ww * 0.5F, wy, 2.0F, 2.0F,
				RoseTheme.withAlpha(RoseTheme.INK_100, walpha), false);
		}

		// subtitle
		float sp = Anim.easeOutCubic(Anim.progress(startTime + 0.3F, 0.7F, now));
		if (sp > 0.02F) {
			String sub = "CLIENT  ·  1.21.11  ·  FABRIC";
			float sw = RoseDraw.textSpacedWidth(sub, 1.0F, 0.8F);
			RoseDraw.textSpaced(g, sub, cx - sw * 0.5F, wy + 20.0F, 1.0F, 0.8F,
				RoseTheme.withAlpha(RoseTheme.INK_40, (int) (255.0F * sp)), false);
		}
	}

	private void renderButtons(GuiGraphics g, float now) {
		float bx = buttonX();

		for (int i = 0; i < BUTTON_KEYS.length; i++) {
			float p = Anim.easeOutCubic(Anim.progress(startTime + 0.32F + i * 0.06F, 0.55F, now));
			float hv = hover[i];
			float y = buttonY(i) + (1.0F - p) * 12.0F;
			int alpha = (int) (255.0F * p);

			if (alpha <= 4) {
				continue;
			}

			int baseTop = RoseTheme.mix(RoseTheme.withAlpha(0xFF1B0D15, 190), RoseTheme.withAlpha(0xFF3A1220, 205), hv);
			int baseBottom = RoseTheme.mix(RoseTheme.withAlpha(0xFF0B0509, 190), RoseTheme.withAlpha(0xFF16070D, 205), hv);

			// hover glow
			if (hv > 0.01F) {
				RoseDraw.rrect(g, bx - 3, y - 3, BUTTON_W + 6, BUTTON_H + 6, 13.0F,
					RoseTheme.withAlpha(RoseTheme.RUBY, (int) (18.0F * hv)),
					RoseTheme.withAlpha(RoseTheme.RUBY, (int) (10.0F * hv)));
			}

			RoseDraw.rrect(g, bx, y, BUTTON_W, BUTTON_H, 11.0F,
				RoseTheme.scaleAlpha(baseTop, alpha / 255.0F),
				RoseTheme.scaleAlpha(baseBottom, alpha / 255.0F));

			// top sheen
			RoseDraw.gradientV(g, bx + 2, y + 1.5F, BUTTON_W - 4, BUTTON_H * 0.5F,
				RoseTheme.withAlpha(0xFFFFFFFF, (int) ((14.0F + 22.0F * hv) * alpha / 255.0F)),
				RoseTheme.withAlpha(0xFFFFFFFF, 0));

			// left accent bar
			RoseDraw.rrect(g, bx + 5, y + BUTTON_H * 0.5F - 6.0F, 2.0F, 12.0F, 1.0F,
				RoseTheme.withAlpha(RoseTheme.RUBY_BRIGHT, (int) ((40.0F + 170.0F * hv) * alpha / 255.0F)),
				RoseTheme.withAlpha(RoseTheme.RUBY, (int) ((30.0F + 150.0F * hv) * alpha / 255.0F)));

			// hairline
			RoseDraw.rrectOutline(g, bx + 0.5F, y + 0.5F, BUTTON_W - 1, BUTTON_H - 1, 10.5F, 1.0F,
				RoseTheme.withAlpha(0xFFFFFFFF, (int) ((20.0F + 40.0F * hv) * alpha / 255.0F)));

			String label = Component.translatable(BUTTON_KEYS[i]).getString();
			int textColor = RoseTheme.mix(RoseTheme.withAlpha(RoseTheme.INK_80, alpha),
				RoseTheme.withAlpha(RoseTheme.INK_100, alpha), hv);

			RoseDraw.textCentered(g, label, bx + BUTTON_W * 0.5F + 3.0F, y + (BUTTON_H - 8.0F) * 0.5F,
				textColor, false);
		}
	}

	private void renderFooter(GuiGraphics g) {
		String left = "Rose Client v" + Rose.VERSION;
		String right = "RShift — ClickGUI";

		RoseDraw.text(g, left, 8.0F, this.height - 12.0F, RoseTheme.withAlpha(RoseTheme.INK_40, 190), false);
		RoseDraw.text(g, right, this.width - 8.0F - RoseDraw.textWidth(right), this.height - 12.0F,
			RoseTheme.withAlpha(RoseTheme.INK_40, 190), false);
	}

	// ------------------------------------------------------------- input

	@Override
	public boolean mouseClicked(MouseButtonEvent context, boolean doubleClick) {
		int index = hitTest(context.x(), context.y());

		if (index >= 0 && context.button() == 0) {
			activate(index);
			return true;
		}

		return super.mouseClicked(context, doubleClick);
	}

	private void activate(int index) {
		if (this.minecraft == null) {
			return;
		}

		switch (index) {
			case 0 -> this.minecraft.setScreen(new SelectWorldScreen(this));
			case 1 -> this.minecraft.setScreen(new JoinMultiplayerScreen(this));
			case 2 -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
			case 3 -> this.minecraft.setScreen(new RoseClickGui());
			case 4 -> this.minecraft.stop();
			default -> {
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return true;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public void onClose() {
		// the main menu stays open
	}
}
