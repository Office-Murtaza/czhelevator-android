package com.kingyon.elevator.interfaces;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public interface IOnAuthKeyPrepared {
    void onResult(String passwordDigestUsed, boolean isSuccess);
}
