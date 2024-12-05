package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;



public class CruiseControlApp extends Application {

    private static final int MAX_DATA_POINTS = 50; // Максимальное количество точек данных на графике
    private XYChart.Series<Number, Number> series;
    private CruiseControl cruiseControl;
    private long lastUpdateTime;
    private TextField speedInput;

    @Override
    public void start(Stage stage) {
        // Настройка осей графика
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Время (сек)");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Скорость (км/ч)");

        // Создание линии графика
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Круиз-контроль");

        // Серия данных для графика
        series = new XYChart.Series<>();
        series.setName("Скорость");

        // Добавляем серию к графику
        lineChart.getData().add(series);

        // Создаем форму для изменения скорости
        HBox controlPanel = createControlPanel();

        // Размещение элементов в BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(lineChart);
        root.setBottom(controlPanel);
        BorderPane.setMargin(controlPanel, new Insets(10)); // Отступы для панели управления

        // Создаем сцену и добавляем график
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        // Инициализация круиз-контроля
        cruiseControl = new CruiseControl(100,0); // Начальная целевая скорость 100 км/ч

        // Запуск таймера для обновления данных каждые 200 мс
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastUpdateTime + 200_000_000L) {
                    updateGraph(now);
                    lastUpdateTime = now;
                }
            }
        };
        timer.start();
    }

    private HBox createControlPanel() {
        speedInput = new TextField();
        speedInput.setPromptText("Новая целевая скорость");
        speedInput.setMaxWidth(150);

        Button changeSpeedButton = new Button("Изменить скорость");
        changeSpeedButton.setOnAction(event -> {
            try {
                double newSpeed = Double.parseDouble(speedInput.getText());
                cruiseControl.setTargetSpeed(newSpeed);
                speedInput.clear(); // Очищаем поле ввода
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат числа: " + e.getMessage());
            }
        });

        HBox panel = new HBox(10, speedInput, changeSpeedButton);
        panel.setPadding(new Insets(10));
        return panel;
    }

    private void updateGraph(long now) {
        // Получаем текущую скорость
        double speed = cruiseControl.getCurrentSpeed();

        // Преобразуем текущее время в секунды
        double timeInSeconds = (now - lastUpdateTime) / 1e9;

        // Добавляем новую точку данных
        series.getData().add(new XYChart.Data<>(timeInSeconds, speed));

        // Ограничиваем количество точек данных на графике
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0);
        }

        // Обновляем состояние круиз-контроля
        cruiseControl.update();
    }

    public static void main(String[] args) {
        launch(args);
    }
}