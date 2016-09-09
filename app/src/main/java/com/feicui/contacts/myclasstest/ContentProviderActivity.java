package com.feicui.contacts.myclasstest;


import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author cpp 2016-09-06
 * @description 内容提供者功能演示demo类
 */
public class ContentProviderActivity extends BaseActivity {
    //用来获取的数据内容
    TextView tv_data;
    EditText et_search;
    Button bt_search;
    ListView lv_cursor;
    SimpleCursorAdapter mSimpleCursorAdapter;
    @Override
    protected int setContent() {
        return R.layout.activity_content_provider;
    }

    @Override
    protected void initView() {
        tv_data = (TextView) findViewById(R.id.tv_data);
        et_search = (EditText) findViewById(R.id.et_search);
        bt_search = (Button) findViewById(R.id.bt_search);
        lv_cursor = (ListView) findViewById(R.id.lv_cursor);
        //insertNewImage();
        //updateImage();
        //deleteImages();
        batchOperateContacts();
    }

    @Override
    protected void setListener() {

    }
    /**
     * @description 使用ContentProvider的方式查询手机中的图片信息
     */
    public void queryImage(View view){
        //查询条件语句
        String mSlectionClaus = null;
        //查询条件的参数
        String[] mSlectionArgs = {""};
        //用户输入的查询关键字
        String mSearch;
        String[] mProjection = new String[] {
            MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };
        //如若查询条件（WHERE）为空，则返回所有的列
        if (TextUtils.isEmpty(mSearch = et_search.getText().toString())) {
            mSlectionClaus = null;
            mSlectionArgs = null;
        }else {
            mSlectionClaus = MediaStore.Images.Media._ID + "= ?";
            mSlectionArgs[0] = mSearch;
        }
        //查询指定uri的ContentProvider，返回一个游标
        Cursor mCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//用来查询公共图片的uri(相当于一个表名)
                mProjection,//相当于COLUMNS,使用“*”可能会报错
                mSlectionClaus,//相当于WHERE
                mSlectionArgs,//相当于WHERE ARGS
                null
        );
        //如若没有正确实例化
        if (mCursor == null) {
        }else if (mCursor.getCount() < 1){
            Toast.makeText(this, "没有匹配的数据，请重新输入！", Toast.LENGTH_SHORT).show();
        }else {
            int idIndex = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int displayIndex = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            //游标移动到第一行，否则游标的位置会在-1停留
            mCursor.moveToFirst();
            //用来存储临时拼接数据的字符串
            String temp = "";
            do {
                String id = mCursor.getString(idIndex);
                String display = mCursor.getString(displayIndex);
                temp += "id =" + id + "\n" + "display = " + display + "\n\n";
            }while (mCursor.moveToNext());
            //构建简易适配器展示数据的控件列表
            int[] mImageListItems = new int[]{
                    R.id.tv_id,
                    R.id.tv_display
            };
            //初始化简易游标适配器
            mSimpleCursorAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.item_cursor,
                    mCursor,
                    mProjection,
                    mImageListItems,
                    0
            );
            //设置UI
            lv_cursor.setAdapter(mSimpleCursorAdapter);

        }
        }
    /**
     * @description 使用ContentProvider的方式查询手机中的单张图片信息
     *
     */
    private void querySingleImage(){
        //指定查询某一行uri
         Uri singleUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,33);
        //查询指定uri的ContentProvider，返回一个游标
         Cursor mCursor = getContentResolver().query(
                 singleUri,//用来查询公共图片的uri(相当于一个表名)
                 null,
                 null,
                 null,
                 null
         );
        //判断游标状态，不为空且有数据时开始遍历
        if (mCursor != null && mCursor.getCount() > 0){
            //确定需要的数据下标，减少IndexOrThrow的调用，提高效率
            int idIndex = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int heightIndex = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
            //游标移动到第一行，否则游标的位置会在-1停留
            mCursor.moveToFirst();
            //用来存储临时拼接数据的字符串
            String temp = "";
            do {
                String id = mCursor.getString(idIndex);
                String height = mCursor.getString(heightIndex);
                temp += "id = " + id + "\n" + "height = " + height + "\n\n";
            }while (mCursor.moveToNext());
            //关闭游标
            mCursor.close();
            //设置UI
            tv_data.setText(temp);

        }
    }
    private void insertNewImage(){
        //用来保存插入数据返回的uri
        Uri mNewUri = null;
        //一条待插入的数据
        ContentValues mContentValues = new ContentValues();
        //设置插入数据的内容
        mContentValues.put(MediaStore.Images.Media.TITLE, "cpp");
        mContentValues.put(MediaStore.Images.Media.HEIGHT, "200");
        //插入数据返回一条当前数据的uri
        mNewUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                mContentValues
        );
        //返回该条uri的_ID值
        tv_data.setText(ContentUris.parseId(mNewUri) + "");
    }
    private  void updateImage(){
        //构建选择语句
        String mSelectionClause = MediaStore.Images.Media.TITLE + " like ?";
        //构建选择语句条件
        String[] mSelectionArgs = {"cpp"};
        //待更新的值
        ContentValues mContentValues = new ContentValues();
        //设置更新的值的内容
        mContentValues.put(MediaStore.Images.Media.TITLE, "更新");
        //更新数据
        getContentResolver().update(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                mContentValues,
                mSelectionClause,
                mSelectionArgs
        );
    }
    private void deleteImages(){
        int mRowsDeleted = 0;
        String mSelectionClause = MediaStore.Images.Media.TITLE +" like ?";
        String[] mSelectionArgs = {"更新"};
        mRowsDeleted = getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                mSelectionClause,
                mSelectionArgs
        );
        tv_data.setText(mRowsDeleted + "");
    }
    /**
     * @description 批量操作通讯录数据
     */
    private void batchOperateContacts(){
        //定义了一个储存了批量操作的集合
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        //添加一个插入新通讯录数据的操作
        ops.add(ContentProviderOperation
        .newInsert(ContactsContract.RawContacts.CONTENT_URI)
        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
        .build()
        );
        //构建选择语句
        String mSelectionClause = ContactsContract.RawContacts._ID + " =1";
        //添加一个删除某条通讯录数据的操作
        ops.add(ContentProviderOperation
                .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(mSelectionClause, null)
                .build()
        );
        //添加一个通讯录断点查询的操作
        ops.add(
                ContentProviderOperation
                    .newAssertQuery(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(mSelectionClause, null)
                    .withExpectedCount(0)
                    .build()
        );
        ops.add(
                ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.DATA1, "123456")
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .build()
        );

        try{
            //保存批处理的操作结果
            ContentProviderResult[] results =
                    //执行批操作处理
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            //保存操作结果的临时变量
            String temp = "";
            for (int i = 0; i < results.length; i ++){
                temp += results[i].toString() + "\n\n";
            }
            //更新UI
            tv_data.setText(temp);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
