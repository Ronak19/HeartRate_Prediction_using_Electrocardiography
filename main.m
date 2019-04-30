clear all;
warning off;
sample = 1;
[heartrate, minutes] = heartrate_calculation(sample);
threshold = 60;
time = bradycardia_detection(heartrate, threshold);

plot([1:minutes], heartrate);
hold on;
for i = 1:minutes
    if heartrate(i) < 60
        c = 'r';
    else
        c = 'b';
    end
    plot(i, heartrate(i), ['-o', c]);
    hold on;
end
xlabel('Time');
ylabel('Heart rate');

y_pred = bradycardia_prediction_QR(heartrate(1:24))
for i = 25:29
    if y_pred(i-24) < 60
        c = 'k';
    else
        c = 'g';
    end
    plot(i, y_pred(i-24), ['-o', c]);
    hold on;
end
xlabel('Time');
ylabel('Heart rate');
hold off;

