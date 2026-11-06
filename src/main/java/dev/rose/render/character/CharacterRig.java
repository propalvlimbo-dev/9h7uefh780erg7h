package dev.rose.render.character;

import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.rose.anim.Spring;
import dev.rose.render.Shaders;
import dev.rose.render.Surfaces;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.gpu.RoseShader;
import dev.rose.render.style.Corners;
import dev.rose.util.ColorUtil;
import java.util.Random;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;


public final class CharacterRig {
   private static final int COLUMNS = 40;
   private static final int ROWS = 60;
   private static final double STEP = 0.004166666666666667;
   private final CharacterModel model;
   private final int vertexCount;
   private final float[] restX;
   private final float[] restY;
   private final float[] headWeight;
   private final float[] breathWeight;
   private final float[][] tailWeight;
   private final float[][] tailProgress;
   private final float[] outX;
   private final float[] outY;
   private final Random random = new Random();
   private final float[] tipX;
   private final float[] tipY;
   private final float[] tipVelocityX;
   private final float[] tipVelocityY;
   private final Spring headAngle = new Spring(0.0, 0.7, 0.62);
   private final Spring hop = new Spring(0.0, 0.42, 0.42);
   private final Spring lean = new Spring(0.0, 1.1, 0.8);
   private double accumulator;
   private long lastNanos = System.nanoTime();
   private float time;
   private float headTarget;
   private float lookTimer;
   private float idleLook;
   private float nextBlink = 2.2F;
   private float blinkStart = -1.0F;
   private boolean doubleBlink;
   private float originX;
   private float originY;
   private float scale = 1.0F;
   private float lastOriginX = Float.NaN;
   private float lastOriginY;
   private float lastVelocityX;
   private float lastVelocityY;
   private float accelerationX;
   private float accelerationY;
   public float hairPhysics = 1.0F;
   public float motion = 1.0F;

   public CharacterRig(CharacterModel model) {
      this.model = model;
      int columns = 41;
      int rows = 61;
      this.vertexCount = columns * rows;
      this.restX = new float[this.vertexCount];
      this.restY = new float[this.vertexCount];
      this.headWeight = new float[this.vertexCount];
      this.breathWeight = new float[this.vertexCount];
      this.outX = new float[this.vertexCount];
      this.outY = new float[this.vertexCount];
      int tails = model.tails.size();
      this.tailWeight = new float[tails][this.vertexCount];
      this.tailProgress = new float[tails][this.vertexCount];
      this.tipX = new float[tails];
      this.tipY = new float[tails];
      this.tipVelocityX = new float[tails];
      this.tipVelocityY = new float[tails];

      for (int row = 0; row < rows; row++) {
         for (int column = 0; column < columns; column++) {
            int i = row * columns + column;
            float x = model.width * column / 40.0F;
            float y = model.height * row / 60.0F;
            this.restX[i] = x;
            this.restY[i] = y;
            float excluded = 0.0F;

            for (CharacterModel.Box box : model.exclude) {
               excluded = Math.max(excluded, insideBox(x, y, box, 10.0F));
            }

            float head = smoothstep(model.headRadius + 26.0F, model.headRadius - 14.0F, (float)Math.hypot(x - model.headX, y - model.headY))
               * (1.0F - insideBox(x, y, this.bodyBelowNeck(), 8.0F));
            this.headWeight[i] = head;
            this.breathWeight[i] = Math.clamp((model.breathBottom - y) / Math.max(1.0F, model.breathBottom - model.breathTop), 0.0F, 1.0F);
            float hair = model.hair(x, y);

            for (int t = 0; t < tails; t++) {
               CharacterModel.Tail tail = model.tails.get(t);
               float side = Math.clamp(Math.min(x - tail.minX(), tail.maxX() - x) / 14.0F + 0.5F, 0.0F, 1.0F);
               float progress = Math.clamp((y - tail.rootY()) / Math.max(1.0F, tail.tipY() - tail.rootY()), 0.0F, 1.0F);
               float weight = hair * (1.0F - head) * (1.0F - excluded) * side;
               this.tailWeight[t][i] = weight;
               this.tailProgress[t][i] = progress;
               this.headWeight[i] = Math.max(this.headWeight[i], weight * (1.0F - progress) * (1.0F - progress) * 0.85F);
            }
         }
      }
   }

   public CharacterModel model() {
      return this.model;
   }

   public void place(float x, float y, float scale) {
      this.originX = x;
      this.originY = y;
      this.scale = scale;
   }

   public void hop(float strength) {
      this.hop.push(-240.0F * strength);

      for (int t = 0; t < this.tipVelocityX.length; t++) {
         this.tipVelocityY[t] = this.tipVelocityY[t] - 160.0F * strength;
      }
   }

   public void nod(float direction) {
      this.headAngle.push(direction * 0.9);
   }

   public void update(float cursorX, float cursorY) {
      long now = System.nanoTime();
      double frame = Math.min((now - this.lastNanos) / 1.0E9, 0.1);
      this.lastNanos = now;
      float dt = (float)frame;
      if (!Float.isNaN(this.lastOriginX) && !(dt <= 0.0F)) {
         float velocityX = (this.originX - this.lastOriginX) / dt;
         float velocityY = (this.originY - this.lastOriginY) / dt;
         this.accelerationX = Math.clamp((velocityX - this.lastVelocityX) / dt, -40000.0F, 40000.0F) / this.scale;
         this.accelerationY = Math.clamp((velocityY - this.lastVelocityY) / dt, -40000.0F, 40000.0F) / this.scale;
         this.lastVelocityX = velocityX;
         this.lastVelocityY = velocityY;
         this.lastOriginX = this.originX;
         this.lastOriginY = this.originY;
      } else {
         this.lastOriginX = this.originX;
         this.lastOriginY = this.originY;
      }

      this.lookTimer -= dt;
      if (this.lookTimer <= 0.0F) {
         this.idleLook = (this.random.nextFloat() - 0.5F) * 0.08F;
         this.lookTimer = 2.5F + this.random.nextFloat() * 3.5F;
      }

      if (!Float.isNaN(cursorX)) {
         float headScreenX = this.originX + this.model.headX * this.scale;
         float headScreenY = this.originY + this.model.headY * this.scale;
         float dx = Math.clamp((cursorX - headScreenX) / (320.0F * Math.max(this.scale, 0.1F)), -1.0F, 1.0F);
         float dy = Math.clamp((cursorY - headScreenY) / (320.0F * Math.max(this.scale, 0.1F)), -1.0F, 1.0F);
         this.headTarget = dx * 0.07F - dy * dx * 0.02F;
         this.lean.setTarget(dx * 0.004F);
      } else {
         this.headTarget = this.idleLook;
         this.lean.setTarget(0.0);
      }

      this.headAngle.setTarget(this.headTarget * this.motion);

      for (this.accumulator += frame; this.accumulator >= 0.004166666666666667; this.accumulator -= 0.004166666666666667) {
         this.stepPhysics(0.004166667F);
      }

      this.time += dt;
      this.updateBlink(dt);
   }

   private void stepPhysics(float dt) {
      float stiffness = 26.0F;
      float damping = 1.9F;
      float inertia = 0.09F;

      for (int t = 0; t < this.tipX.length; t++) {
         CharacterModel.Tail tail = this.model.tails.get(t);
         float side = Math.signum(tail.tipX() - tail.rootX());
         float wind = (float)(Math.sin(this.time * 0.9 + t * 2.1) * 0.6 + Math.sin(this.time * 2.3 + t) * 0.25) * 9.0F * side;
         float forceX = -stiffness * this.tipX[t] - damping * this.tipVelocityX[t] - this.accelerationX * inertia + wind;
         float forceY = -stiffness * 1.6F * this.tipY[t] - damping * this.tipVelocityY[t] - this.accelerationY * inertia * 0.6F;
         this.tipVelocityX[t] = this.tipVelocityX[t] + forceX * dt;
         this.tipVelocityY[t] = this.tipVelocityY[t] + forceY * dt;
         this.tipX[t] = Math.clamp(this.tipX[t] + this.tipVelocityX[t] * dt, -46.0F, 46.0F);
         this.tipY[t] = Math.clamp(this.tipY[t] + this.tipVelocityY[t] * dt, -30.0F, 30.0F);
      }
   }

   private void updateBlink(float dt) {
      if (this.blinkStart >= 0.0F) {
         if (this.time - this.blinkStart > 0.17F) {
            if (this.doubleBlink) {
               this.doubleBlink = false;
               this.blinkStart = this.time + 0.08F;
            } else {
               this.blinkStart = -1.0F;
            }
         }
      } else {
         this.nextBlink -= dt;
         if (this.nextBlink <= 0.0F) {
            this.blinkStart = this.time;
            this.doubleBlink = this.random.nextFloat() < 0.18F;
            this.nextBlink = 2.4F + this.random.nextFloat() * 3.6F;
         }
      }
   }

   private float blink() {
      if (!(this.blinkStart < 0.0F) && !(this.time < this.blinkStart)) {
         float t = (this.time - this.blinkStart) / 0.17F;
         return t >= 1.0F ? 0.0F : (float)Math.sin(Math.PI * Math.min(1.0F, t * 1.15F));
      } else {
         return 0.0F;
      }
   }

   public float headScreenX() {
      return this.originX + this.model.headX * this.scale;
   }

   public float headScreenY() {
      return this.originY + (this.model.headY + this.hop.getFloat() * this.motion) * this.scale;
   }

   public boolean hit(float screenX, float screenY) {
      return this.model.opaqueAt((screenX - this.originX) / this.scale, (screenY - this.originY) / this.scale);
   }

   public void draw(GuiGraphics g, float alpha, int shadowColor, float shadowOffsetX, float shadowOffsetY) {
      RoseShader shader = Shaders.character();
      if (!(alpha <= 0.001F)) {
         this.deform();
         shader.setSampler("Sampler0", this.model.texture);
         if (ColorUtil.alpha(shadowColor) > 0) {
            float[][] taps = new float[][]{{0.0F, 0.0F}, {-2.5F, 0.0F}, {2.5F, 0.0F}, {0.0F, -2.5F}, {0.0F, 2.5F}};
            shader.safeGetUniform("Silhouette")
               .set(ColorUtil.red(shadowColor) / 255.0F, ColorUtil.green(shadowColor) / 255.0F, ColorUtil.blue(shadowColor) / 255.0F, 1.0F);
            int tapColor = ColorUtil.fade(-1, ColorUtil.alpha(shadowColor) / 255.0F / taps.length * alpha);

            for (float[] tap : taps) {
               this.submit(g, shader, shadowOffsetX + tap[0], shadowOffsetY + tap[1], tapColor);
            }
         }

         shader.safeGetUniform("Silhouette").set(0.0F, 0.0F, 0.0F, 0.0F);
         this.submit(g, shader, 0.0F, 0.0F, ColorUtil.fade(-1, alpha));
         this.drawEyelids(g, alpha);
      }
   }

   private void submit(GuiGraphics g, RoseShader shader, float offsetX, float offsetY, int color) {
      Matrix4f pose = GuiDraw.pose(g);
      BufferBuilder buffer = GuiDraw.begin(shader);
      int columns = 41;

      for (int row = 0; row < 60; row++) {
         float v0 = row / 60.0F;
         float v1 = (row + 1) / 60.0F;

         for (int column = 0; column < 40; column++) {
            float u0 = column / 40.0F;
            float u1 = (column + 1) / 40.0F;
            int a = row * columns + column;
            int b = (row + 1) * columns + column;
            buffer.addVertex(pose, this.outX[a] + offsetX, this.outY[a] + offsetY, 0.0F).setUv(u0, v0).setColor(color);
            buffer.addVertex(pose, this.outX[b] + offsetX, this.outY[b] + offsetY, 0.0F).setUv(u0, v1).setColor(color);
            buffer.addVertex(pose, this.outX[b + 1] + offsetX, this.outY[b + 1] + offsetY, 0.0F).setUv(u1, v1).setColor(color);
            buffer.addVertex(pose, this.outX[a + 1] + offsetX, this.outY[a + 1] + offsetY, 0.0F).setUv(u1, v0).setColor(color);
         }
      }

      GuiDraw.submit(g, shader, Blend.NORMAL, buffer);
   }

   private void deform() {
      float breath = (float)(0.5 - 0.5 * Math.cos(this.time * Math.PI * 2.0 / 3.8)) * this.motion;
      float angle = this.headAngle.getFloat();
      float sway = ((float)Math.sin(this.time * 1.05) * 0.0026F + this.lean.getFloat()) * this.motion;
      float hopOffset = this.hop.getFloat() * this.motion;
      float hair = this.hairPhysics * this.motion;
      float cosSway = (float)Math.cos(sway);
      float sinSway = (float)Math.sin(sway);

      for (int i = 0; i < this.vertexCount; i++) {
         float x = this.restX[i];
         float y = this.restY[i];
         float dx = 0.0F;
         float dy = -this.breathWeight[i] * this.model.breathAmount * breath;

         for (int t = 0; t < this.tipX.length; t++) {
            float weight = this.tailWeight[t][i];
            if (!(weight <= 0.001F)) {
               float progress = this.tailProgress[t][i];
               float bend = (float)Math.pow(progress, 1.5) * weight * hair;
               float wave = (float)Math.sin(this.time * 1.7 - progress * 3.4 + t * 1.3) * 2.2F * progress * weight * hair;
               dx += this.tipX[t] * bend + wave;
               dy += this.tipY[t] * bend;
            }
         }

         float px = x + dx;
         float py = y + dy;
         float turn = angle * this.headWeight[i];
         if (turn != 0.0F) {
            float cos = (float)Math.cos(turn);
            float sin = (float)Math.sin(turn);
            float rx = px - this.model.pivotX;
            float ry = py - this.model.pivotY;
            px = this.model.pivotX + rx * cos - ry * sin;
            py = this.model.pivotY + rx * sin + ry * cos;
         }

         float fx = px - this.model.feetX;
         float fy = py - this.model.feetY;
         px = this.model.feetX + fx * cosSway - fy * sinSway;
         py = this.model.feetY + fx * sinSway + fy * cosSway + hopOffset;
         this.outX[i] = this.originX + px * this.scale;
         this.outY[i] = this.originY + py * this.scale;
      }
   }

   private void drawEyelids(GuiGraphics g, float alpha) {
      float closed = this.blink() * this.motion;
      if (!(closed <= 0.02F) && !this.model.eyes.isEmpty()) {
         float angle = this.headAngle.getFloat();

         for (CharacterModel.Eye eye : this.model.eyes) {
            float[] point = this.deformPoint(eye.x(), eye.y());
            float w = eye.w() * this.scale;
            float h = eye.h() * this.scale;
            g.pose().pushMatrix();
            g.pose().translate(point[0], point[1]);
            g.pose().rotate(this.model.eyeAngle + angle);
            float lidH = h * Math.min(1.0F, closed * 1.05F);
            int skin = ColorUtil.fade(this.model.skinColor, alpha);
            Surfaces.fill(g, -w / 2.0F - 1.0F, -h / 2.0F - 1.0F, w + 2.0F, lidH + 1.0F, Corners.round(Math.min(w, h) * 0.3F), skin, skin, skin, skin, 1.0F);
            int lash = ColorUtil.fade(this.model.lashColor, alpha * Math.min(1.0F, closed * 1.6F));
            float lashY = -h / 2.0F + lidH * 0.92F;
            Surfaces.fill(g, -w / 2.0F - 0.5F, lashY - 0.6F, w + 1.0F, 1.3F, Corners.round(0.65F), lash, lash, lash, lash, 1.0F);
            g.pose().popMatrix();
         }
      }
   }

   private float[] deformPoint(float x, float y) {
      float breath = (float)(0.5 - 0.5 * Math.cos(this.time * Math.PI * 2.0 / 3.8)) * this.motion;
      float breathWeightHere = Math.clamp((this.model.breathBottom - y) / Math.max(1.0F, this.model.breathBottom - this.model.breathTop), 0.0F, 1.0F);
      float px = x;
      float py = y - breathWeightHere * this.model.breathAmount * breath;
      float turn = this.headAngle.getFloat();
      float cos = (float)Math.cos(turn);
      float sin = (float)Math.sin(turn);
      float rx = px - this.model.pivotX;
      float ry = py - this.model.pivotY;
      px = this.model.pivotX + rx * cos - ry * sin;
      py = this.model.pivotY + rx * sin + ry * cos;
      float sway = ((float)Math.sin(this.time * 1.05) * 0.0026F + this.lean.getFloat()) * this.motion;
      float fx = px - this.model.feetX;
      float fy = py - this.model.feetY;
      px = this.model.feetX + fx * (float)Math.cos(sway) - fy * (float)Math.sin(sway);
      py = this.model.feetY + fx * (float)Math.sin(sway) + fy * (float)Math.cos(sway) + this.hop.getFloat() * this.motion;
      return new float[]{this.originX + px * this.scale, this.originY + py * this.scale};
   }

   private CharacterModel.Box bodyBelowNeck() {
      return new CharacterModel.Box(0.0F, this.model.pivotY + 4.0F, this.model.width, this.model.height);
   }

   private static float insideBox(float x, float y, CharacterModel.Box box, float feather) {
      float inside = Math.min(Math.min(x - box.x0(), box.x1() - x), Math.min(y - box.y0(), box.y1() - y));
      return Math.clamp(inside / feather + 0.5F, 0.0F, 1.0F);
   }

   private static float smoothstep(float edge0, float edge1, float x) {
      float t = Math.clamp((x - edge0) / (edge1 - edge0), 0.0F, 1.0F);
      return t * t * (3.0F - 2.0F * t);
   }
}
