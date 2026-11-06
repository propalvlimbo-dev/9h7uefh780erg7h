package dev.rose.setting;

import com.google.gson.JsonElement;
import dev.rose.config.ConfigManager;
import dev.rose.module.Module;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import net.minecraft.client.resources.language.I18n;


public abstract class Setting<T> {
   private final String id;
   private final T defaultValue;
   private T value;
   private Module owner;
   private String section = "";
   private String translationBase;
   private BooleanSupplier visibility = () -> true;
   private Runnable listener = () -> {};

   protected Setting(String id, T defaultValue) {
      this.id = id;
      this.defaultValue = defaultValue;
      this.value = defaultValue;
   }

   public final String id() {
      return this.id;
   }

   public String name() {
      return I18n.get(this.translationKey(), new Object[0]);
   }

   public String description() {
      String key = this.translationKey() + ".description";
      return I18n.exists(key) ? I18n.get(key, new Object[0]) : "";
   }

   protected final String translationKey() {
      return this.translationBase != null
         ? this.translationBase + "." + this.id
         : "rose.module." + (this.owner == null ? "unknown" : this.owner.id()) + "." + this.id;
   }

   public <S extends Setting<T>> S translatedAs(String base) {
      this.translationBase = base;
      return (S)(Object)this;
   }

   public T get() {
      return this.value;
   }

   public T defaultValue() {
      return this.defaultValue;
   }

   public void set(T newValue) {
      T sanitized = this.sanitize(newValue);
      if (!Objects.equals(this.value, sanitized)) {
         this.value = sanitized;
         this.listener.run();
         ConfigManager.markDirty();
      }
   }

   public void reset() {
      this.set(this.defaultValue);
   }

   public boolean isDefault() {
      return Objects.equals(this.value, this.defaultValue);
   }

   protected T sanitize(T newValue) {
      return newValue;
   }

   public boolean isVisible() {
      return this.visibility.getAsBoolean();
   }

   public <S extends Setting<T>> S visibleWhen(BooleanSupplier condition) {
      this.visibility = condition;
      return (S)(Object)this;
   }

   public <S extends Setting<T>> S onChange(Runnable action) {
      this.listener = action;
      return (S)(Object)this;
   }

   public void attach(Module module) {
      this.owner = module;
   }

   public Module owner() {
      return this.owner;
   }

   public String section() {
      return this.section;
   }

   public void setSection(String section) {
      this.section = section;
   }

   public abstract JsonElement toJson();

   public abstract void fromJson(JsonElement var1);
}
