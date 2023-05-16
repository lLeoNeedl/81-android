package com.nile.fortu.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.nile.fortu.game.databinding.ActivityBonusGameBinding
import com.nile.fortu.game.databinding.ActivityFirstGameBinding
import com.nile.fortu.game.slotImagesScroll.EventEnd
import com.nile.fortu.game.slotImagesScroll.SlotScroll
import com.nile.fortu.game.slotImagesScroll.Utils
import kotlin.random.Random

class FirstGameActivity : AppCompatActivity(), EventEnd {

    private var countDown = 0

    private lateinit var image1: SlotScroll
    private lateinit var image2:  SlotScroll
    private lateinit var image3:  SlotScroll
    private lateinit var flSpin: FrameLayout
    private lateinit var btnReturn: ImageButton
    private lateinit var leverDown: ImageView
    private lateinit var leverUp: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_game)

        initViews()

        image1.setEventEnd(this@FirstGameActivity)
        image2.setEventEnd(this@FirstGameActivity)
        image3.setEventEnd(this@FirstGameActivity)

        flSpin.setOnClickListener {
            if (Utils.score >= 50) {
                leverUp.visibility = View.GONE
                leverDown.visibility = View.VISIBLE
                image1.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                image2.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                image3.setRandomValue(Random.nextInt(6), Random.nextInt(15 - 5 + 1) + 5)
                Utils.score -= 50
//                var score = binding.frameLayout.findViewById<TextView>(R.id.score)
//                score.text = Utils.score.toString()
            } else {
                Toast.makeText(this, "You don't have enough money", Toast.LENGTH_SHORT).show()
            }
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, FirstGameActivity::class.java)
    }



    private fun initViews (){
        image1 = findViewById(R.id.image1)
        image2 = findViewById(R.id.image2)
        image3 = findViewById(R.id.image3)
        flSpin = findViewById(R.id.fl_spin)
        btnReturn = findViewById(R.id.btn_return)
        leverDown = findViewById(R.id.leverDown)
        leverUp = findViewById(R.id.leverUp)
    }

    override fun eventEnd(result: Int, count: Int) {
        if(countDown < 2){
            countDown++
        }
        else{
            leverDown.visibility = View.GONE
            leverUp.visibility = View.VISIBLE
            countDown = 0

            if(image1.value == image2.value && image2.value == image3.value){
                Toast.makeText(this,"YOU WON!!!!", Toast.LENGTH_SHORT).show()
                Utils.score +=300
//                score_tv.text = Utils.score.toString()
           }
            else if(image1.value == image2.value || image2.value == image3.value || image1.value == image3.value){
                Toast.makeText(this,"You did good.", Toast.LENGTH_SHORT).show()
                Utils.score +=100
//                score_tv.text = Utils.score.toString()
            }
            else{
                Toast.makeText(this,"You lost. Better luck next time.", Toast.LENGTH_SHORT).show()
                Utils.score +=0
//                score_tv.text = Utils.score.toString()
            }
        }
    }
}