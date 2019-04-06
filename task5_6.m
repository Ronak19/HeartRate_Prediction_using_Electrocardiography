p = dlmread('ekg_raw_16272.dat');
RawECG = p(:,2);
minutes = (p(length(p),1) - p(1,1))/60;
[index, peaks] = RPeakDetection(RawECG);
% [peaks,index] = findpeaks(RawECG)
plot(RawECG);
hold on;
plot(index,peaks,'o');
hold off;
overall_heartrate = length(index)/minutes;
% plot(Rpeak_index, Rpeak_values);
j = 1;
heartrate = [];
for i = 1:minutes
    start1 = j;
    while p(j,1) <= i* 60 + 3600
        if p(j,1) == i* 60 + 3600
            end1 = j;
        end
        j = j+1;
    end
    [index, peaks] = RPeakDetection(p(start1:end1,2));
    heartrate = [heartrate, length(index)];
end

plot([1:minutes], heartrate);