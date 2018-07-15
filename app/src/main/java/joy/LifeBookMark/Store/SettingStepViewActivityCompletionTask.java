package joy.LifeBookMark.Store;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import joy.LifeBookMark.LifeBookMarkTabMain;
import joy.LifeBookMark.SettingStepViewActivity.CompletionTask;

class SettingStepViewActivityCompletionTask implements OnClickListener {
    final /* synthetic */ CompletionTask this$1;
    final /* synthetic */ Dialog val$dialog;

    SettingStepViewActivityCompletionTask(CompletionTask this$1, Dialog dialog) {
        this.this$1 = this$1;
        this.val$dialog = dialog;
    }

    public void onClick(View v) {
        this.this$1.this$0.startActivity(new Intent(this.this$1.this$0, LifeBookMarkTabMain.class));
        this.val$dialog.cancel();
    }
}
