function y_pred = bradycardia_prediction_QR(y)

% [p,S] = polyfit([1:24],y,3);
% [y_pred,delta] = polyval(p,[25:29],S);

    lm = fitlm(transpose([y(2:23);y(1:22)]),y(3:24)','quadratic');

    y_prev = [y(23),y(24)];
    for i = 1:length([25:29])
        y_prev = [y_prev, predict(lm,transpose([y_prev(i+1);y_prev(i)]))];
    end
    y_pred = y_prev(3:end);