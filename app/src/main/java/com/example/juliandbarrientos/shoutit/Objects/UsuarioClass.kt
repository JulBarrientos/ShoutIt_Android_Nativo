package com.example.juliandbarrientos.shoutit.Objects

import java.util.*

/**
 * Created by julian.d.barrientos on 11/6/2017.
 */
class UsuarioClass (name: String ="",  email: String ="", imageUrl : String =""){

    var _name: String = name

    var _email: String = email

    var _imageUrl: String = imageUrl

    var _key: String =""

    override fun equals(other: Any?): Boolean {
        return if(other is UsuarioClass)
            this._key == other._key
        else
            return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = _name.hashCode()
        result = 31 * result + _email.hashCode()
        result = 31 * result + _imageUrl.hashCode()
        result = 31 * result + _key.hashCode()
        return result
    }

}