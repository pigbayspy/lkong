package io.pig.lkong.broadcast

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.http.data.resp.data.NoticeRespData
import io.pig.lkong.ui.notify.NotifyActivity

class CheckNewBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val noticeCountModel: NoticeRespData =
            intent.getParcelableExtra(DataContract.BUNDLE_NOTICE_COUNT_MODEL)!!
        if (noticeCountModel.hasNotice()) {
            showNewNoticeNotification(
                context,
                noticeCountModel
            )
        }
    }

    private fun showNewNoticeNotification(context: Context, noticeCount: NoticeRespData) {
        val stringBuilder = StringBuilder(60)
        if (noticeCount.atmeCount > 0) {
            stringBuilder.append(
                context.getString(
                    R.string.format_notice_mentions,
                    noticeCount.atmeCount
                )
            )
        }
        if (noticeCount.noticeCount > 0) {
            stringBuilder
                .append(if (stringBuilder.isNotEmpty()) "," else "")
                .append(
                    context.getString(
                        R.string.format_notice_notice,
                        noticeCount.noticeCount
                    )
                )
        }
        if (noticeCount.pmCount > 0) {
            stringBuilder
                .append(if (stringBuilder.isNotEmpty()) "," else "")
                .append(
                    context.getString(
                        R.string.format_notice_private_message,
                        noticeCount.pmCount
                    )
                )
        }
        if (noticeCount.rateCount > 0) {
            stringBuilder
                .append(if (stringBuilder.isNotEmpty()) "," else "")
                .append(
                    context.getString(
                        R.string.format_notice_rate,
                        noticeCount.rateCount
                    )
                )
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mResultBuilder = NotificationCompat.Builder(context, NOTIFY_CHANNEL_ID)
        val openNotificationActivityIntent = Intent(context, NotifyActivity::class.java)
        val chaptersListIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            openNotificationActivityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )
        val extras: Bundle = Bundle.EMPTY
        mResultBuilder.setContentTitle(
            context.getString(
                R.string.format_notice_all_count,
                noticeCount.getAllNoticeCount()
            )
        )
            .setContentText(stringBuilder.toString())
            .setSmallIcon(R.drawable.ic_notification_lkong_logo)
            .setExtras(extras)
            .setContentIntent(chaptersListIntent)
            .setAutoCancel(true)
        notificationManager.notify(NOTIFICATION_START_ID, mResultBuilder.build())
    }

    companion object {
        const val NOTIFICATION_START_ID = 150

        const val NOTIFY_CHANNEL_ID = "io.pig.lkong.NOTIFY_CHANNEL_ID"
    }
}