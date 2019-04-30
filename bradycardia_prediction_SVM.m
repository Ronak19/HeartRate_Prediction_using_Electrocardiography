function y_pred = bradycardia_prediction_SVM(y)

% [p,S] = polyfit([1:24],y,3);
% [y_pred,delta] = polyval(p,[25:29],S);

    lab = [];
    for i=1:length(y(1:24))
        if y(i) < 60
            lab = [lab, 1];
        else
            lab = [lab, -1];
        end
    end

    svm = fitcsvm(transpose([lab(2:23);lab(1:22)]), lab(3:24)','KernelFunction','linear');

    y_prev = [lab(23),lab(24)];
    for i = 1:length([25:29])
        y_prev = [y_prev, predict(svm,transpose([y_prev(i+1);y_prev(i)]))];
    end
    y_pred = y_prev(3:end);