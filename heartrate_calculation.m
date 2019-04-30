function [heartrate, minutes] = heartrate_calculation(k)
    datfiles = dir('*.dat');
%     for k = 1:length(datfiles)

        p = dlmread(datfiles(k).name);
        RawECG = p(:,2);
        minutes = (p(length(p),1) - p(1,1))/60;
        [index, peaks] = RPeakDetection(RawECG);
%         xlswrite('Rpeaks4.xlsx', index','A1:A1');
%         for m = 1:length(index)
%             xlswrite('Rpeaks4.xlsx', p(index(m),1),['B',num2str(m),':B',num2str(m)]);
%         end
        fid = fopen(['Rpeaks',num2str(k),'.txt'],'w');
        for t1 = 1:length(index)
            fprintf(fid, '%s ', num2str(index(t1)));
        end
        fclose(fid);
        % [peaks,index] = findpeaks(RawECG)
    %     figure(k);
    %     plot(RawECG);
    %     hold on;
    %     plot(index,peaks,'o');
    %     hold off;
        overall_heartrate = length(index)/minutes
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
            if exist(['d',num2str(k),'/Rpeaks',num2str(k),'_',num2str(i),'.txt'])
                delete(['d',num2str(k),'/Rpeaks',num2str(k),'_',num2str(i),'.txt']);
            end
            fid = fopen(['d',num2str(k),'/Rpeaks',num2str(k),'_',num2str(i),'.txt'],'w');
            for t1 = 1:length(index)
                fprintf(fid, '%s ', num2str(index(t1)+start1-1));
            end
        fclose(fid);
        end

%     end