package com.zxzhu.show.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zxzhu.show.model.IUserModel;
import com.zxzhu.show.model.UserModel;
import com.zxzhu.show.view.Inference.ILoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zxzhu on 2017/8/16.
 */

public class LoginPresenter implements ILoginPresenter {
    private IUserModel userModel = new UserModel();
    private ILoginActivity loginActivity;

    public LoginPresenter(ILoginActivity activity) {
        loginActivity = activity;
        PlatformConfig.setQQZone("1106280483", "gLiXoc06apTtfyw8");
        PlatformConfig.setSinaWeibo("2203336036", "6b0ac8d8c371124eee648b7657840c4d","www.baidu.com");
        Config.DEBUG = true;
    }
    @Override
    public void login(final String username, final String password) {
        loginActivity.showDialog();
        Log.d("loginPresenter", "login: "+username);
        userModel.login(username, password, new UserModel.UserListener() {
            @Override
            public void onSuccess() {
                loginActivity.hideDialog();
                loginActivity.jumpToMain();
        }

            @Override
            public void onError(AVException e) {
                loginActivity.hideDialog();
                if (e.getCode() == 211) {
                    loginActivity.toast("用户名或密码错误");
                }
                Log.d("loginPresenter", "onError: "+ e);
            }
        });
    }

    @Override
    public void setQQLogin() {
        UMShareAPI.get(loginActivity.getActivity()).getPlatformInfo(loginActivity.getActivity(), SHARE_MEDIA.QQ,new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                loginActivity.showDialog();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                JSONObject userMes = null;
                if (map != null) {
                    try {
                        userMes = new JSONObject(map);
                        final String nickName = userMes.getString("name");
                        final String iconUrl = userMes.getString("iconurl");
                        final String openId = userMes.getString("uid");
                        Log.d("openId is",openId);
                        userModel.qqSign(openId, nickName, iconUrl, null, new UserModel.UserListener() {
                            @Override
                            public void onSuccess() {
                                userModel.login("QQ用户"+openId, openId, new UserModel.UserListener() {
                                    @Override
                                    public void onSuccess() {
                                        loginActivity.hideDialog();
                                        loginActivity.jumpToMain();
                                    }

                                    @Override
                                    public void onError(AVException e) {
                                        loginActivity.hideDialog();
                                        Log.d("loginPresenter", "onError: "+ e);
                                    }
                                });
                            }

                            @Override
                            public void onError(AVException e) {
                                userModel.login("QQ用户"+openId, openId, new UserModel.UserListener() {
                                    @Override
                                    public void onSuccess() {
                                        loginActivity.hideDialog();
                                        loginActivity.jumpToMain();
                                    }

                                    @Override
                                    public void onError(AVException e) {
                                        loginActivity.hideDialog();
                                        Log.d("loginPresenter", "onError: "+ e);
                                    }
                                });
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                loginActivity.hideDialog();
                Log.d("zzx", "onError: "+throwable.toString());
                loginActivity.toast("error"+i);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                loginActivity.hideDialog();
                Log.d("zzx", "onCancel: ");
                loginActivity.toast("cancel");
            }
        });
    }

    @Override
    public void setWbLogin() {
        UMShareAPI.get(loginActivity.getActivity()).getPlatformInfo(loginActivity.getActivity(), SHARE_MEDIA.SINA,new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                loginActivity.showDialog();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                loginActivity.hideDialog();
                JSONObject userMes = null;
                if (map != null) {
                    try {
                        userMes = new JSONObject(map);
                        final String nickName = userMes.getString("name");
                        final String iconUrl = userMes.getString("iconurl");
                        final String openId = userMes.getString("uid");
                        Log.d("openId is",openId);
                        userModel.wbSign(openId, nickName, iconUrl, null, new UserModel.UserListener() {
                            @Override
                            public void onSuccess() {
                                userModel.login("微博用户"+openId, openId, new UserModel.UserListener() {
                                    @Override
                                    public void onSuccess() {
                                        loginActivity.hideDialog();
                                        loginActivity.jumpToMain();
                                    }

                                    @Override
                                    public void onError(AVException e) {
                                        loginActivity.hideDialog();
                                        Log.d("zzzz", "onError: "+ e);
                                    }
                                });
                            }

                            @Override
                            public void onError(AVException e) {
                                userModel.login("微博用户"+openId, openId, new UserModel.UserListener() {
                                    @Override
                                    public void onSuccess() {
                                        loginActivity.hideDialog();
                                        loginActivity.jumpToMain();
                                    }

                                    @Override
                                    public void onError(AVException e) {
                                        loginActivity.hideDialog();
                                        Log.d("loginPresenter", "onError: "+ e);
                                    }
                                });
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                loginActivity.hideDialog();
                loginActivity.toast(throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                loginActivity.hideDialog();
            }
        });
    }

}
