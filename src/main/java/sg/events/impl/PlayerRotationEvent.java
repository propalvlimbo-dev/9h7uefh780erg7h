package sg.events.impl;

import lombok.Generated;
import org.joml.Vector2f;
import sg.events.ClientEvent;

public class PlayerRotationEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("В")  Vector2f rotation;

   @Generated
   public Vector2f getRotation() {
      return this.rotation;
   }

   @Generated
   public PlayerRotationEvent(Vector2f rotation) {
      this.rotation = rotation;
   }
}
