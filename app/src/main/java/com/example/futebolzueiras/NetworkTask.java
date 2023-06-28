/*package com.example.futebolzueiras;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

p   private class NetworkTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("descricao", "Objeto T");
                jsonObject.put("time", "Time T");

                String encodedImage = Base64.encodeToString(imageViewToByte(imageView), Base64.DEFAULT);
                jsonObject.put("imagem", encodedImage);

                // Log the JSON string using Logcat
                Log.d("JSON", jsonObject.toString());

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

                Request request = new Request.Builder()
                        .url("https://api.jsonbin.io/v3/b/649b3153b89b1e2299b623f4")
                        .put(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("X-Master-Key", "$2b$10$Ro77nGY6r/wTBGKein45f.0aupOB9O6dKKE.VGYhsm6JN2lnEtYFa")
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    // Envio bem-sucedido
                    String responseBody = response.body().string();
                    Log.d("RESPONSE", responseBody);
                    return responseBody;
                } else {
                    // O envio falhou
                    Log.e("ERROR", "HTTP Error: " + response.code());
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Envio bem-sucedido
                Toast.makeText(requireContext(), "Envio bem-sucedido", Toast.LENGTH_SHORT).show();
                // Após registrar seta os campos para default
                et_description.setText("");
                et_tag.setText("");
                imageView.setImageResource(R.drawable.random_guest);
            } else {
                // O envio falhou
                Toast.makeText(requireContext(), "Erro ao enviar a solicitação", Toast.LENGTH_SHORT).show();
            }
        }
    }*/