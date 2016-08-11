package com.ileonidze.jchat;

import java.util.Date;

public class SelfService { // will return bytes?
    public int CollectDBGarbage(){
        int currentTimestamp = Math.round(new Date().getTime()/1000);
        int collected = 0;
        for(int i=0;i<VDB.sessions.size();i++){
            VDBSession someSession = VDB.sessions.get(i);
            if(currentTimestamp-someSession.getUsedTimestamp()>259200){ // 259200 seconds = 3 days
                collected += ObjectSizeFetcher.getObjectSize(someSession);
                VDB.sessions.remove(i);
            }
        }
        return collected;
    }

    public void proceed(){
        // TODO: flush DB to disk!
        // TODO: Write service statistic as garbage collected
        CollectDBGarbage();
    }
}