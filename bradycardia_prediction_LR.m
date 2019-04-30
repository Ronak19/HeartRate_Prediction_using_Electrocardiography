function [y_pred,delta] = bradycardia_prediction_LR(y)

% [p,S] = polyfit([1:24],y,3);
% [y_pred,delta] = polyval(p,[25:29],S);

    lm = fitlm(transpose([y(2:23);y(1:22)]),y(3:24)','linear');

    y_prev = [y(23),y(24)];
    for i = 1:length([25:29])
        y_prev = [y_prev, predict(lm,transpose([y_prev(i+1);y_prev(i)]))];
    end
    y_pred = y_prev(3:end);
% mean1 = [];
% % std1 = [];
% for i = 1:21
%     mean1 = [mean1, mean(y(1:i))];
% %     std1 = [std1, std(y(1:i))];
% end
% lm = fitlm(transpose([y(3:23);y(2:22);mean1]),y(4:24)','quadratic');
% 
% mean1 = [mean1, mean(y(1:22)), mean(y(1:23)), mean(y(1:24))];
% % std1 = [std1, std(y(1:22)), std(y(1:23)), std(y(1:24))];
% y_prev = [y(23),y(24)];
% for i = 1:length([25:29])
%     if i>3
%         mean1 = [mean1, mean([y(1:24),y_prev(1:i)])];
% %         std1 = [std1, std([y(1:24),y_prev(1:i)])];
%     end
%     y_prev = [y_prev, predict(lm,transpose([y_prev(i+1);y_prev(i);mean1(i+21)]))];
% end
% y_pred = y_prev(3:end);

