package com.example.test_moq_android


import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URI


class QuicClient(quicUri: String) {
    final var TAG: String = "QUIC Webtransport Client"
    private var outputStream : OutputStream
    private var inputStream : InputStream

    init {


        Log.i(TAG, "Successfully Built HTTP Client.")
        try {
            client = Http3Client.newBuilder().build()

            if (client.isConnected)
                Log.i(TAG, "Successfully established connection with server")

        }catch (err : IOException){
            Log.e(TAG, err.message!!)
        }

        stream = client.createStream(false)

        Log.i(TAG, "Successfully created stream with id " + stream.streamId)
        inputStream = stream.inputStream
        outputStream = stream.outputStream

    }

    fun getOutputStream(): OutputStream{
        return this.outputStream
    }

    fun getInputStream(): InputStream {
        return this.inputStream
    }


}