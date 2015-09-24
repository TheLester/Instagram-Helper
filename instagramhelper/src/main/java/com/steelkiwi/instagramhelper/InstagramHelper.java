package com.steelkiwi.instagramhelper;

import android.app.Dialog;
import android.content.Context;

import java.text.MessageFormat;

import static com.steelkiwi.instagramhelper.utils.Constants.AUTH_URL;
import static com.steelkiwi.instagramhelper.utils.Constants.CLIENT_ID_DEF;
import static com.steelkiwi.instagramhelper.utils.Constants.REDIRECT_URI_DEF;
import static com.steelkiwi.instagramhelper.utils.Constants.RESPONSE_TYPE_DEF;
import static com.steelkiwi.instagramhelper.utils.Utils.checkNotNull;

public class InstagramHelper {

    public interface AuthorizeListener {
        void onSuccess(String token);

        void onError(String message);

        void onCancel();
    }


    private String clientId;
    private String redirectUri;
    private Dialog instagramWebDialog;

    private InstagramHelper(String clientId,String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public void showAuthorizeDialog(Context context, AuthorizeListener listener) {
        String authUrl = MessageFormat.format(AUTH_URL + CLIENT_ID_DEF + "{0}" + REDIRECT_URI_DEF + "{1}" + RESPONSE_TYPE_DEF, clientId, redirectUri);
        if (instagramWebDialog == null) {
            instagramWebDialog = new InstagramWebDialog(context, authUrl, redirectUri, listener);
        }
        instagramWebDialog.show();
    }

    public static final class Builder {
        private String  clientId;
        private String  redirectUrl;

        public Builder withClientId(String clientId) {
            this.clientId = checkNotNull(clientId, "clientId == null");
            return this;
        }

        public Builder withRedirectUrl(String redirectUrl) {
            this.redirectUrl = checkNotNull(redirectUrl, "redirectUrl == null");
            return this;
        }

        public InstagramHelper build() {
            return new InstagramHelper(clientId, redirectUrl);
        }
    }
}
