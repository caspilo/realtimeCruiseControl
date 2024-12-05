package org.example;
import java.util.Random;

public class CruiseControl {

    private double targetSpeed; // Целевая скорость
    private double currentSpeed; // Текущая скорость
    private final Random random = new Random();

    public CruiseControl(double initialTargetSpeed, double initalCurrentSpeed) {
        this.targetSpeed = initialTargetSpeed;
        this.currentSpeed = initalCurrentSpeed;
    }

    public void update() {
        // Обновление текущей скорости с учетом случайных отклонений
        currentSpeed += (random.nextDouble() * 0.05 - 0.025);

        if (currentSpeed > targetSpeed + 10) { // Критическая ситуация
            System.out.println("Критическая ситуация! Скорость слишком высока.");
        }
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setTargetSpeed(double newTargetSpeed) {
        this.targetSpeed = newTargetSpeed;
    }
}