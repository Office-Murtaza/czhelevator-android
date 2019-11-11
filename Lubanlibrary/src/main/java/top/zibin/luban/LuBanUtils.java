package top.zibin.luban;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.leo.afbaselibrary.nets.exceptions.ResultException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by gongli on 2017/6/30 10:12
 * email: lc824767150@163.com
 */

public class LuBanUtils {

    /**
     * RxAndroid异步压缩一个图片文件
     *
     * @param context
     * @param srcFile
     * @return Observable<List<File>>
     */
    public static Observable<List<File>> compressImage(final Context context, File srcFile) {
        List<File> files = new ArrayList<>();
        files.add(srcFile);
        return compressImages(context, files);
    }

    /**
     * RxAndroid异步根据路径压缩一张图片
     *
     * @param context
     * @param toString 此对象的toString方法需要返回图片路径，否则程序崩溃
     * @return Observable<List<File>>
     */
    public static Observable<List<File>> compressImageWithPath(final Context context, Object toString) {
        List<Object> files = new ArrayList<>();
        files.add(toString);
        return compressImagesWithPath(context, files);
    }

    /**
     * RxAndroid异步压缩一个图片集
     *
     * @param context
     * @param srcFiles
     * @return Observable<List<File>>
     */
    public static Observable<List<File>> compressImages(final Context context, List<File> srcFiles) {
        return Observable.just(srcFiles).map(new Func1<List<File>, List<File>>() {
            @Override
            public List<File> call(List<File> srcFiles) {
                try {
                    if (srcFiles == null || srcFiles.size() < 1) {
                        throw new ResultException(205, "LuBanUtils compress image error");
                    }
                    List<File> dstFiles = new ArrayList<>();
                    for (File srcfile : srcFiles) {
                        if (srcfile == null || !srcfile.exists() || srcfile.isDirectory()) {
                            throw new ResultException(205, "LuBanUtils compress image error");
                        }
                        File dstFile = Luban.with(context).load(srcfile).get();
                        dstFiles.add(dstFile);
                    }
                    return dstFiles;
                } catch (IOException e) {
                    throw new ResultException(205, "LuBanUtils compress image error");
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io());
    }

    /**
     * RxAndroid异步根据一个路径集压缩图片
     *
     * @param context
     * @param toStrings 此集合中的对象的toString方法需要返回图片路径，否则程序崩溃
     * @return Observable<List<File>>
     */
    public static Observable<List<File>> compressImagesWithPath(final Context context, List<Object> toStrings) {
        return Observable.just(toStrings).map(new Func1<List<Object>, List<File>>() {
            @Override
            public List<File> call(List<Object> srcFilePathes) {
                if (srcFilePathes == null || srcFilePathes.size() < 1) {
                    throw new ResultException(205, "LuBanUtils compress image error");
                }
                List<File> dstFiles = new ArrayList<>();
                for (Object obj : srcFilePathes) {
                    if (obj == null) {
                        throw new ResultException(205, "LuBanUtils compress image error");
                    }
                    String srcFilePath = obj.toString();
                    if (TextUtils.isEmpty(srcFilePath)) {
                        throw new ResultException(205, "LuBanUtils compress image error");
                    }
                    File srcFile = new File(srcFilePath);
                    if (!srcFile.exists() || srcFile.isDirectory()) {
                        throw new ResultException(205, "LuBanUtils compress image error");
                    }
                    dstFiles.add(srcFile);
                }
                return dstFiles;
            }
        }).flatMap(new Func1<List<File>, Observable<List<File>>>() {
            @Override
            public Observable<List<File>> call(List<File> files) {
                return compressImages(context, files);
            }
        });
//        return Observable.just(srcFilePathes).map(new Func1<List<String>, List<File>>() {
//            @Override
//            public List<File> call(List<String> srcFilePathes) {
//                try {
//                    if (srcFilePathes == null || srcFilePathes.size() < 1) {
//                        throw new ResultException(205, "LuBanUtils compress image error");
//                    }
//                    List<File> dstFiles = new ArrayList<>();
//                    for (String srcfilePath : srcFilePathes) {
//                        if (TextUtils.isEmpty(srcfilePath)) {
//                            throw new ResultException(205, "LuBanUtils compress image error");
//                        }
//                        File srcFile = new File(srcfilePath);
//                        if (!srcFile.exists() || srcFile.isDirectory()) {
//                            throw new ResultException(205, "LuBanUtils compress image error");
//                        }
//                        File dstFile = Luban.with(context).load(srcFile).get();
//                        dstFiles.add(dstFile);
//                    }
//                    return dstFiles;
//                } catch (IOException e) {
//                    throw new ResultException(205, "LuBanUtils compress image error");
//                }
//            }
//        });
    }

    /**
     * RxAndroid异步压缩一个图片集
     *
     * @param context
     * @param srcFiles
     * @return Observable<HashMap<String, RequestBody>>
     */
    public static Observable<HashMap<String, RequestBody>> compressImagesToRequestBody(final Context context, List<File> srcFiles) {
        return getRequestBody(compressImages(context, srcFiles));
    }

    /**
     * RxAndroid异步根据一个路径集压缩图片
     *
     * @param context
     * @param srcFilePathes
     * @return Observable<HashMap<String, RequestBody>>
     */
    public static Observable<HashMap<String, RequestBody>> compressImagesWithPathToRequestBody(final Context context, List<Object> srcFilePathes) {
        return getRequestBody(compressImagesWithPath(context, srcFilePathes));
    }

    /**
     * RxAndroid异步把图片集转换为RequestBody
     *
     * @param observable
     * @return Observable<HashMap<String, RequestBody>>
     */
    private static Observable<HashMap<String, RequestBody>> getRequestBody(Observable<List<File>> observable) {
        return observable.map(new Func1<List<File>, HashMap<String, RequestBody>>() {
            @Override
            public HashMap<String, RequestBody> call(List<File> files) {
                HashMap<String, RequestBody> body = new HashMap<>();
                for (File file : files) {
                    body.put("file\";fileName=\"" + file.getName() + "", RequestBody.create(MediaType.parse("image/png"), file));
                }
                return body;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io());
    }

    /**
     * 清除压缩后的文件
     *
     * @param context
     */
    public static void clear(Context context) {
        Luban.with(context).clear();
    }

    /**
     * 清除压缩后的文件
     *
     * @param context
     * @param luBanClearListener
     */
    public static void clearAsync(Context context, LuBanClearListener luBanClearListener) {
        Luban.with(context).clearAsync(luBanClearListener);
    }

    public static long getCacheSize(Context context) {
        return Luban.with(context).getCacheSize();
    }
}
