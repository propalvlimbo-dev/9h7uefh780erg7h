package sg.gui.manager.now;

public final class AnimatedValue {
    private final long duration;
    private double current;
    private double target;
    private double start;
    private long startTime;

    public AnimatedValue(long durationMs) {
        this.duration = Math.max(1L, durationMs);
        this.current = 0.0;
        this.target = 0.0;
        this.start = 0.0;
        this.startTime = System.currentTimeMillis();
    }

    public void processData(double newTarget) {
        if (Math.abs(newTarget - this.target) > 1e-6) {
            this.start = computeDouble();
            this.target = newTarget;
            this.startTime = System.currentTimeMillis();
        }
    }

    public AnimatedValue getAnimatedValue(double instant) {
        this.start = instant;
        this.target = instant;
        this.current = instant;
        this.startTime = System.currentTimeMillis();
        return this;
    }

    public double computeDouble() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= duration) {
            current = target;
            return current;
        }
        double t = Math.clamp(elapsed / (double) duration, 0.0, 1.0);
        // Smooth cubic ease-out curve
        double ease = 1.0 - Math.pow(1.0 - t, 3.0);
        current = start + (target - start) * ease;
        return current;
    }

    public double computeDouble2() {
        return this.target;
    }
}
