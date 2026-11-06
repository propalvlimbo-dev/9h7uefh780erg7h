package sg.events.impl;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.StagedVertexBuffer.Draw;
import net.minecraft.client.renderer.StagedVertexBuffer.ExecuteInfo;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderType;

public interface WorldGeometryBuffer {
   VertexConsumer getBuffer(RenderType renderType);

   static WorldGeometryBuffer.StagedBuffer getShared() {
      return WorldGeometryBuffer.Holder.INSTANCE;
   }

   final class Holder {
      private static final WorldGeometryBuffer.StagedBuffer INSTANCE = new WorldGeometryBuffer.StagedBuffer();
   }

   final class StagedBuffer implements AutoCloseable, WorldGeometryBuffer {
      @SerializedName("В")
      private final StagedVertexBuffer stagedVertexBuffer;
      private final @com.google.gson.annotations.SerializedName("сЬ")  List<Draw> draws;
      private final @com.google.gson.annotations.SerializedName("с衣")  List<PreparedRenderType> preparedRenderTypes;
      private @com.google.gson.annotations.SerializedName("е")  RenderType currentRenderType;
      @SerializedName("В")
      private Draw currentDraw;
      private static final int ЯЦэ = 786432;
      private static final String ЯЦю = "Rose geometry";

      @Override
      public VertexConsumer getBuffer(RenderType renderType) {
         if (this.currentRenderType != renderType || this.currentDraw == null || !renderType.canConsolidateConsecutiveGeometry()) {
            this.currentRenderType = renderType;
            this.currentDraw = this.stagedVertexBuffer
               .appendDraw(renderType.format(), renderType.primitiveTopology(), renderType.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null);
            this.draws.add(this.currentDraw);
            this.preparedRenderTypes.add(renderType.prepare());
         }

         return this.stagedVertexBuffer.getVertexBuilder(this.currentDraw);
      }

      public void flush(RenderType ignoredRenderType) {
         this.flush();
      }

      public void finishFrame() {
         this.flush();
         this.stagedVertexBuffer.endFrame();
      }

      public StagedBuffer() {
         this.stagedVertexBuffer = new StagedVertexBuffer(() -> ЯЦю, ЯЦэ);
         this.draws = new ArrayList<>();
         this.preparedRenderTypes = new ArrayList<>();
      }

      @Override
      public void close() {
         this.stagedVertexBuffer.close();
         this.draws.clear();
         this.preparedRenderTypes.clear();
         this.currentRenderType = null;
         this.currentDraw = null;
      }

      public void flush() {
         if (!this.draws.isEmpty()) {
            try {
               this.stagedVertexBuffer.upload();

               for (int drawIndex = 0; drawIndex < this.draws.size(); drawIndex++) {
                  ExecuteInfo executeInfo = this.stagedVertexBuffer.getExecuteInfo(this.draws.get(drawIndex));
                  if (executeInfo != null) {
                     this.preparedRenderTypes.get(drawIndex).drawFromBuffer(executeInfo);
                  }
               }
            } finally {
               this.stagedVertexBuffer.endDraw();
               this.draws.clear();
               this.preparedRenderTypes.clear();
               this.currentRenderType = null;
               this.currentDraw = null;
            }
         }
      }
   }
}
