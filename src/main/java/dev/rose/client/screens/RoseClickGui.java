package dev.rose.client.screens;

import dev.rose.client.RoseClient;
import dev.rose.client.config.RoseConfig;
import dev.rose.client.module.Module;
import dev.rose.client.module.Setting;
import dev.rose.client.render.RoseDraw;
import dev.rose.client.render.RoseTheme;
import dev.rose.client.ui.Anim;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Rose ClickGUI: glass window, animated, driven by the module metadata
 * (every setting automatically becomes a slider).
 */
public class RoseClickGui extends Screen {
	private static final Identifier LOGO = Identifier.fromNamespaceAndPath("rose", "textures/gui/logo_32.png");

	private static final float WIN_W = 452.0F;
	private static final float WIN_H = 272.0F;
	private static final float HEADER_H = 38.0F;
	private static final float SIDEBAR_W = 108.0F;
	private static final float ROW_H = 34.0F;
	private static final float ROW_GAP = 6.0F;
	private static final float SETTING_H = 24.0F;

	private final Map<String, Float> expand = new HashMap<>();
	private final Map<String, Float> toggleAnim = new HashMap<>();
	private final List<RowHit> rows = new ArrayList<>();
	private final List<SliderHit> sliders = new ArrayList<>();

	private float winX;
	private float winY;
	private float dragOffX;
	private float dragOffY;

	private boolean draggingWindow;
	private boolean mouseDown;
	private Setting activeSlider;
	private float activeSliderX;
	private float activeSliderW;
	private float mouseX;
	private float mouseY;

	private float openTime;
	private float lastTime;
	private float dt;
	private float open;

	private int hoverRow = -1;
	private int hoverCategory = -1;
	private boolean hoverClose;

	private String selectedCategory = "Visuals";

	public RoseClickGui() {
		super(Component.literal("Rose"));
	}

	@Override
	protected void init() {
		super.init();

		openTime = Anim.now();
		lastTime = openTime;

		if (winX <= 0.0F && winY <= 0.0F) {
			winX = (this.width - WIN_W) * 0.5F;
			winY = (this.height - WIN_H) * 0.5F;
		}
	}

	// -------------------------------------------------------------- layout

	private void layout() {
		rows.clear();
		sliders.clear();

		List<Module> modules = RoseClient.MODULES.byCategory(selectedCategory);
		float x = winX + SIDEBAR_W + 12.0F;
		float w = WIN_W - SIDEBAR_W - 24.0F;
		float y = winY + HEADER_H + 12.0F;

		for (Module module : modules) {
			RowHit row = new RowHit();
			row.module = module;
			row.x = x;
			row.y = y;
			row.w = w;
			row.h = ROW_H;
			row.switchX = x + w - 40.0F;
			row.switchY = y + ROW_H * 0.5F - 7.0F;
			row.switchW = 28.0F;
			row.switchH = 14.0F;
			rows.add(row);

			y += ROW_H + ROW_GAP;

			float e = expandOf(module);
			if (e > 0.01F) {
				float pad = 4.0F;
				y += pad * e;

				for (Setting setting : module.settings()) {
					SliderHit hit = new SliderHit();
					hit.setting = setting;
					hit.x = x + 14.0F;
					hit.w = w - 28.0F;
					hit.y = y;
					sliders.add(hit);
					y += SETTING_H * e;
				}

				y += pad * e;
			}
		}
	}

	private float expandOf(Module module) {
		return expand.getOrDefault(module.id, 0.0F);
	}

	private float toggleOf(Module module) {
		return toggleAnim.getOrDefault(module.id, module.isEnabled() ? 1.0F : 0.0F);
	}

	private boolean inside(float px, float py, float x, float y, float w, float h) {
		return px >= x && px <= x + w && py >= y && py <= y + h;
	}

	// ------------------------------------------------------------ render

	@Override
	public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		float now = Anim.now();
		dt = Math.min(0.1F, Math.max(0.0F, now - lastTime));
		lastTime = now;

		open = Anim.approach(open, 1.0F, 12.0F, dt);

		this.mouseX = mouseX;
		this.mouseY = mouseY;

		if (draggingWindow && mouseDown) {
			winX = Math.max(4.0F, Math.min(this.width - WIN_W - 4.0F, mouseX - dragOffX));
			winY = Math.max(4.0F, Math.min(this.height - WIN_H - 4.0F, mouseY - dragOffY));
		}

		layout();
		updateHover(mouseX, mouseY);

		// animate expansion / toggles
		for (Module module : RoseClient.MODULES.modules()) {
			expand.put(module.id, Anim.approach(expandOf(module), expandTarget(module), 14.0F, dt));
			toggleAnim.put(module.id, Anim.approach(toggleOf(module), module.isEnabled() ? 1.0F : 0.0F, 18.0F, dt));
		}

		if (mouseDown && activeSlider != null && activeSliderW > 0.0F) {
			float progress = (mouseX - activeSliderX) / activeSliderW;
			activeSlider.setProgress(progress);
		}

		renderBackground(g, mouseX, mouseY, partialTick);

		g.pose().pushMatrix();
		float scale = 0.94F + 0.06F * Anim.easeOutQuint(open);
		float cx = winX + WIN_W * 0.5F;
		float cy = winY + WIN_H * 0.5F;
		g.pose().translate(cx, cy);
		g.pose().scale(scale);
		g.pose().translate(-cx, -cy);

		renderWindow(g, now);
		g.pose().popMatrix();
	}

	@Override
	public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
		// dim the world behind, keep it visible (glass needs something to refract later)
		RoseDraw.gradientV(g, 0, 0, this.width, this.height, 0x99100609, 0x99100410);

		float cx = winX + WIN_W * 0.5F;
		float cy = winY + WIN_H * 0.5F;
		RoseDraw.glow(g, cx, cy, Math.max(this.width, this.height) * 0.45F, 0x14E4294B, 14);

		RoseDraw.gradientV(g, 0, this.height * 0.62F, this.width, this.height * 0.38F, 0x00000000, 0x77000000);
	}

	private void renderWindow(GuiGraphics g, float now) {
		int alpha = (int) (255.0F * Anim.easeOutCubic(open));

		RoseDraw.panel(g, winX, winY, WIN_W, WIN_H, RoseTheme.RADIUS_LG, (int) (215.0F * Anim.easeOutCubic(open)));

		// header
		RoseDraw.gradientV(g, winX + 1, winY + 1, WIN_W - 2, HEADER_H - 2,
			RoseTheme.withAlpha(0xFFFFFFFF, (int) (18.0F * alpha / 255.0F)),
			RoseTheme.withAlpha(0xFFFFFFFF, 0));

		RoseDraw.image(g, LOGO, winX + 12.0F, winY + 4.0F, 32,
			RoseTheme.withAlpha(0xFFFFFFFF, alpha));

		RoseDraw.textSpaced(g, "ROSE", winX + 48.0F, winY + 13.0F, 1.0F, 1.2F,
			RoseTheme.withAlpha(RoseTheme.INK_100, alpha), false);

		String sub = "v" + dev.rose.Rose.VERSION + "  ·  glass ui";
		RoseDraw.text(g, sub, winX + 48.0F + RoseDraw.textSpacedWidth("ROSE", 1.0F, 1.2F) + 8.0F, winY + 14.0F,
			RoseTheme.withAlpha(RoseTheme.INK_40, alpha), false);

		// close button
		float closeX = winX + WIN_W - 26.0F;
		float closeY = winY + 8.0F;
		int closeColor = hoverClose ? RoseTheme.RUBY_BRIGHT : RoseTheme.INK_40;

		RoseDraw.circle(g, closeX + 9.0F, closeY + 9.0F, 9.0F,
			RoseTheme.withAlpha(hoverClose ? 0x33E4294B : 0x14FFFFFF, alpha));
		RoseDraw.line(g, closeX + 5.5F, closeY + 5.5F, closeX + 12.5F, closeY + 12.5F, 1.6F,
			RoseTheme.withAlpha(closeColor, alpha));
		RoseDraw.line(g, closeX + 12.5F, closeY + 5.5F, closeX + 5.5F, closeY + 12.5F, 1.6F,
			RoseTheme.withAlpha(closeColor, alpha));

		// separator
		RoseDraw.gradientH(g, winX + 10.0F, winY + HEADER_H, WIN_W - 20.0F, 1.0F,
			RoseTheme.withAlpha(0xFFFFFFFF, 0), RoseTheme.withAlpha(0xFFFFFFFF, (int) (26.0F * alpha / 255.0F)));
		RoseDraw.gradientH(g, winX + WIN_W * 0.5F, winY + HEADER_H, WIN_W * 0.5F - 10.0F, 1.0F,
			RoseTheme.withAlpha(0xFFFFFFFF, (int) (26.0F * alpha / 255.0F)), RoseTheme.withAlpha(0xFFFFFFFF, 0));

		// sidebar
		renderSidebar(g, alpha);

		// content
		renderRows(g, alpha);

		// footer hint
		String hint = "RShift / Esc to close";
		RoseDraw.text(g, hint, winX + WIN_W - 12.0F - RoseDraw.textWidth(hint), winY + WIN_H - 14.0F,
			RoseTheme.withAlpha(RoseTheme.INK_40, (int) (150.0F * alpha / 255.0F)), false);
	}

	private void renderSidebar(GuiGraphics g, int alpha) {
		List<String> categories = RoseClient.MODULES.categories();
		float x = winX + 8.0F;
		float y = winY + HEADER_H + 12.0F;
		float w = SIDEBAR_W - 16.0F;

		for (int i = 0; i < categories.size(); i++) {
			String category = categories.get(i);
			boolean active = category.equals(selectedCategory);
			boolean hoveredCategory = hoverCategory == i;
			float h = 26.0F;

			int bg = active
				? RoseTheme.withAlpha(0x4DE4294B, alpha)
				: RoseTheme.withAlpha(hoveredCategory ? 0x1AFFFFFF : 0x0AFFFFFF, alpha);

			RoseDraw.rrectSolid(g, x, y, w, h, RoseTheme.RADIUS_SM, bg);

			if (active) {
				RoseDraw.rrect(g, x, y, 2.5F, h, 1.25F,
					RoseTheme.withAlpha(RoseTheme.RUBY_BRIGHT, alpha), RoseTheme.withAlpha(RoseTheme.RUBY, alpha));
			}

			RoseDraw.text(g, category, x + 12.0F, y + 9.0F,
				RoseTheme.withAlpha(active ? RoseTheme.INK_100 : RoseTheme.INK_60, alpha), false);

			int count = RoseClient.MODULES.byCategory(category).size();
			String countText = String.valueOf(count);
			RoseDraw.text(g, countText, x + w - 10.0F - RoseDraw.textWidth(countText), y + 9.0F,
				RoseTheme.withAlpha(RoseTheme.INK_40, alpha), false);

			y += h + 5.0F;
		}
	}

	private void renderRows(GuiGraphics g, int alpha) {
		for (int i = 0; i < rows.size(); i++) {
			RowHit row = rows.get(i);
			Module module = row.module;
			boolean hoveredRow = hoverRow == i;
			float expandValue = expandOf(module);

			// card
			int cardTop = RoseTheme.withAlpha(0xFF201018, alpha);
			int cardBottom = RoseTheme.withAlpha(0xFF0E0609, alpha);

			if (hoveredRow) {
				cardTop = RoseTheme.withAlpha(0xFF2E1622, alpha);
				cardBottom = RoseTheme.withAlpha(0xFF14080E, alpha);
			}

			RoseDraw.rrect(g, row.x, row.y, row.w, row.h, RoseTheme.RADIUS_MD, cardTop, cardBottom);
			RoseDraw.rrectOutline(g, row.x + 0.5F, row.y + 0.5F, row.w - 1, row.h - 1, RoseTheme.RADIUS_MD - 0.5F,
				1.0F, RoseTheme.withAlpha(0xFFFFFFFF, (int) ((hoveredRow ? 44.0F : 24.0F) * alpha / 255.0F)));

			// name + description
			RoseDraw.text(g, module.name, row.x + 12.0F, row.y + 7.0F, RoseTheme.withAlpha(RoseTheme.INK_100, alpha), false);
			RoseDraw.text(g, module.description, row.x + 12.0F, row.y + 19.0F,
				RoseTheme.withAlpha(RoseTheme.INK_40, alpha), false);

			// switch
			float t = toggleOf(module);
			int trackBg = RoseTheme.mix(RoseTheme.withAlpha(0xFF2A2A31, alpha), RoseTheme.withAlpha(RoseTheme.RUBY_DEEP, alpha), t);

			RoseDraw.rrectSolid(g, row.switchX, row.switchY, row.switchW, row.switchH, row.switchH * 0.5F, trackBg);
			RoseDraw.rrectOutline(g, row.switchX + 0.5F, row.switchY + 0.5F, row.switchW - 1, row.switchH - 1,
				row.switchH * 0.5F, 1.0F, RoseTheme.withAlpha(0xFFFFFFFF, (int) ((30.0F + 40.0F * t) * alpha / 255.0F)));

			float knobR = row.switchH * 0.5F - 2.5F;
			float knobX = row.switchX + knobR + 2.5F + t * (row.switchW - knobR * 2.0F - 5.0F);
			float knobY = row.switchY + row.switchH * 0.5F;

			if (t > 0.05F) {
				RoseDraw.glow(g, knobX, knobY, knobR * 2.6F, RoseTheme.withAlpha(RoseTheme.RUBY, (int) (70.0F * t)), 8);
			}

			RoseDraw.circle(g, knobX, knobY, knobR, RoseTheme.withAlpha(0xFFFFFFFF, alpha));

			// chevron
			float chX = row.x + row.w - 14.0F;
			float chY = row.y + ROW_H * 0.5F;
			float dir = expandValue > 0.5F ? -1.0F : 1.0F;
			int chColor = RoseTheme.withAlpha(RoseTheme.INK_60, alpha);
			RoseDraw.line(g, chX - 3.0F, chY - 2.0F * dir, chX, chY + 2.0F * dir, 1.4F, chColor);
			RoseDraw.line(g, chX, chY + 2.0F * dir, chX + 3.0F, chY - 2.0F * dir, 1.4F, chColor);
		}

		// settings (sliders)
		for (SliderHit slider : sliders) {
			renderSlider(g, slider, alpha);
		}
	}

	private void renderSlider(GuiGraphics g, SliderHit hit, int alpha) {
		Setting setting = hit.setting;
		float progress = setting.getProgress();
		float y = hit.y;

		RoseDraw.text(g, setting.name, hit.x, y + 1.0F, RoseTheme.withAlpha(RoseTheme.INK_60, alpha), false);

		String value = setting.displayValue();
		RoseDraw.text(g, value, hit.x + hit.w - RoseDraw.textWidth(value), y + 1.0F,
			RoseTheme.withAlpha(RoseTheme.RUBY_BRIGHT, alpha), false);

		float trackY = y + 14.0F;
		RoseDraw.rrectSolid(g, hit.x, trackY, hit.w, 3.0F, 1.5F, RoseTheme.withAlpha(0x33FFFFFF, alpha));
		RoseDraw.rrect(g, hit.x, trackY, Math.max(3.0F, hit.w * progress), 3.0F, 1.5F,
			RoseTheme.withAlpha(RoseTheme.RUBY_BRIGHT, alpha), RoseTheme.withAlpha(RoseTheme.RUBY_DEEP, alpha));

		float knobX = hit.x + hit.w * progress;
		boolean active = activeSlider == setting;

		RoseDraw.glow(g, knobX, trackY + 1.5F, active ? 12.0F : 8.0F,
			RoseTheme.withAlpha(RoseTheme.RUBY, (int) ((active ? 110.0F : 60.0F) * alpha / 255.0F)), 8);
		RoseDraw.circle(g, knobX, trackY + 1.5F, active ? 4.0F : 3.4F, RoseTheme.withAlpha(0xFFFFFFFF, alpha));
	}

	// ------------------------------------------------------------- input

	private void updateHover(float mouseX, float mouseY) {
		hoverRow = -1;
		hoverCategory = -1;

		for (int i = 0; i < rows.size(); i++) {
			RowHit row = rows.get(i);
			if (inside(mouseX, mouseY, row.x, row.y, row.w, row.h)) {
				hoverRow = i;
				break;
			}
		}

		List<String> categories = RoseClient.MODULES.categories();
		for (int i = 0; i < categories.size(); i++) {
			float x = winX + 8.0F;
			float y = winY + HEADER_H + 12.0F + i * 31.0F;
			if (inside(mouseX, mouseY, x, y, SIDEBAR_W - 16.0F, 26.0F)) {
				hoverCategory = i;
				break;
			}
		}

		hoverClose = inside(mouseX, mouseY, winX + WIN_W - 26.0F, winY + 8.0F, 18.0F, 18.0F);
	}

	private float expandTarget(Module module) {
		return expand.getOrDefault(module.id + ":target", 0.0F);
	}

	private void setExpandTarget(Module module, float target) {
		expand.put(module.id + ":target", target);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent context, boolean doubleClick) {
		if (open < 0.8F) {
			return false;
		}

		float mx = (float) context.x();
		float my = (float) context.y();

		if (context.button() != 0) {
			return super.mouseClicked(context, doubleClick);
		}

		mouseDown = true;

		if (hoverClose) {
			onClose();
			return true;
		}

		// header drag
		if (inside(mx, my, winX, winY, WIN_W, HEADER_H)) {
			draggingWindow = true;
			dragOffX = mx - winX;
			dragOffY = my - winY;
			return true;
		}

		// categories
		List<String> categories = RoseClient.MODULES.categories();
		for (int i = 0; i < categories.size(); i++) {
			float x = winX + 8.0F;
			float y = winY + HEADER_H + 12.0F + i * 31.0F;

			if (inside(mx, my, x, y, SIDEBAR_W - 16.0F, 26.0F)) {
				selectedCategory = categories.get(i);
				return true;
			}
		}

		// sliders first (they are inside expanded rows)
		for (SliderHit slider : sliders) {
			if (inside(mx, my, slider.x - 4.0F, slider.y, slider.w + 8.0F, SETTING_H)) {
				activeSlider = slider.setting;
				activeSliderX = slider.x;
				activeSliderW = slider.w;
				activeSlider.setProgress((mx - slider.x) / slider.w);
				return true;
			}
		}

		// module rows
		for (RowHit row : rows) {
			if (!inside(mx, my, row.x, row.y, row.w, row.h)) {
				continue;
			}

			if (inside(mx, my, row.switchX - 4.0F, row.switchY - 3.0F, row.switchW + 8.0F, row.switchH + 6.0F)) {
				row.module.toggle();
			} else {
				float target = expandTarget(row.module) > 0.5F ? 0.0F : 1.0F;
				setExpandTarget(row.module, target);
			}

			return true;
		}

		return super.mouseClicked(context, doubleClick);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent context) {
		mouseDown = false;

		if (activeSlider != null) {
			activeSlider = null;
			RoseConfig.save();
		}

		draggingWindow = false;
		return super.mouseReleased(context);
	}

	@Override
	public boolean keyPressed(KeyEvent context) {
		if (context.key() == GLFW.GLFW_KEY_ESCAPE || context.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			onClose();
			return true;
		}

		return super.keyPressed(context);
	}

	@Override
	public void onClose() {
		RoseConfig.save();

		if (this.minecraft == null) {
			return;
		}

		if (this.minecraft.level == null) {
			this.minecraft.setScreen(new RoseTitleScreen());
		} else {
			this.minecraft.setScreen(null);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	// ------------------------------------------------------------- holders

	private static final class RowHit {
		Module module;
		float x;
		float y;
		float w;
		float h;
		float switchX;
		float switchY;
		float switchW;
		float switchH;
	}

	private static final class SliderHit {
		Setting setting;
		float x;
		float y;
		float w;
	}
}
