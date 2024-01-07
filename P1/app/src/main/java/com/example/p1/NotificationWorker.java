package com.example.p1;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {
	private Context context;

	public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
		this.context = context;
	}

	@SuppressLint("MissingPermission")
	@NonNull
	@Override
	public Result doWork() {
		//Añade el extra recompensa (monedas) cuando pulsas sobre la notificacion llevandote a la actividad principal
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra("reward", true);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "game_channel")
				.setSmallIcon(R.drawable.ic_notification)
				.setContentTitle("¡Vuelve y gana 20 monedas!")
				.setContentText("Te echamos de menos")
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setContentIntent(contentIntent)
				.setAutoCancel(true);

		NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
		managerCompat.notify(1, builder.build());
		return Result.success();
	}
}
