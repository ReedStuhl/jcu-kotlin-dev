package com.example.jupe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.collections.ArrayList
import kotlin.random.Random

class game : AppCompatActivity() {

    val cardImages: IntArray = intArrayOf(
            R.drawable.clubstwo,
            R.drawable.clubsthree,
            R.drawable.clubsfour,
            R.drawable.clubsfive,
            R.drawable.clubssix,
            R.drawable.clubsseven,
            R.drawable.clubseight,
            R.drawable.clubsnine,
            R.drawable.clubsten,
            R.drawable.clubsjack,
            R.drawable.clubsqueen,
            R.drawable.clubsking,
            R.drawable.clubsace,
            R.drawable.diamondstwo,
            R.drawable.diamondsthree,
            R.drawable.diamondsfour,
            R.drawable.diamondsfive,
            R.drawable.diamondssix,
            R.drawable.diamondsseven,
            R.drawable.diamondseight,
            R.drawable.diamondsnine,
            R.drawable.diamondsten,
            R.drawable.diamondsjack,
            R.drawable.diamondsqueen,
            R.drawable.diamondsking,
            R.drawable.diamondsace,
            R.drawable.heartstwo,
            R.drawable.heartsthree,
            R.drawable.heartsfour,
            R.drawable.heartsfive,
            R.drawable.heartssix,
            R.drawable.heartsseven,
            R.drawable.heartseight,
            R.drawable.heartsnine,
            R.drawable.heartsten,
            R.drawable.heartsjack,
            R.drawable.heartsqueen,
            R.drawable.heartsking,
            R.drawable.heartsace,
            R.drawable.spadestwo,
            R.drawable.spadesthree,
            R.drawable.spadesfour,
            R.drawable.spadesfive,
            R.drawable.spadessix,
            R.drawable.spadesseven,
            R.drawable.spadeseight,
            R.drawable.spadesnine,
            R.drawable.spadesten,
            R.drawable.spadesjack,
            R.drawable.spadesqueen,
            R.drawable.spadesking,
            R.drawable.spadesace) // 52 cards, no jokers.
    private val cards = ArrayList<Int>() // 14 card values, added below.
    var suit = 0 // counter to change suits of cards dealt
    private val pHand = ArrayList<Int>() // player hand, 4 cards.
    private val cHand = ArrayList<Int>() // computer hand, 4 cards.
    private val deck = ArrayList<Int>() // this is the burn pile, used to keep track of the top card
    var pScore = 0 // player score
    var cScore = 0 // computer score
    var stacked = false // checks if a card was stacked
    var mustDeal = true // makes jupe button un spamable
    var turn = false // check for whos turn it is
    var swapped = false // checks if top card was swapped
    var gg = false // checks for endgame
    var turns = 0 // once 22 end of round

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // all possible values
        cards.add(0); cards.add(1); cards.add(2); cards.add(3); cards.add(4)
        cards.add(5); cards.add(6); cards.add(7); cards.add(8); cards.add(9); cards.add(10)
        cards.add(11); cards.add(12); cards.add(13)
        val bl = findViewById<ImageView>(R.id.bl) // player cards
        val br = findViewById<ImageView>(R.id.br)
        val tl = findViewById<ImageView>(R.id.tl)
        val tr = findViewById<ImageView>(R.id.tr)
        val card = findViewById<ImageView>(R.id.card) // top burn card image
        val draw = findViewById<Button>(R.id.draw) // draws a card button
        val deal = findViewById<Button>(R.id.deal) // deals cards button
        val done = findViewById<Button>(R.id.done) // end turn button
        val comment = findViewById<TextView>(R.id.comment) // cpu text box
        val que = findViewById<ImageView>(R.id.que) // que stion button
        val endRound = findViewById<Button>(R.id.round) // jupe button
        val gameOver = findViewById<TextView>(R.id.gameOver) // endgame text box
        val playerP = findViewById<TextView>(R.id.pp) // player score text box
        val compP = findViewById<TextView>(R.id.cp) // computer score text box
        val cBL = findViewById<ImageView>(R.id.cpuBl) // cpu cards
        val cBR = findViewById<ImageView>(R.id.cpuBr)
        val cTL = findViewById<ImageView>(R.id.cpuTl)
        val cTR = findViewById<ImageView>(R.id.cpuTr)
        val random = Random
        val player = arrayOf(bl, br, tl, tr) // array of images on display
        val computer = arrayOf(cBL, cBR, cTL, cTR) // array of computer cards images
        var ds = false // ds = draw and start making all buttons clickable

        /**
         * Deals two 4 card hands with a freshly shuffled deck.
         * It deals the same way most people do giving card by
         * card to each player, not four and four.
         * This method listener also sets the tables images up with
         * the deal button.
         * resets scores once game is over
         */
        deal.setOnClickListener {
            newHand()
            turns = 0
            mustDeal = false
            stacked = false
            comment.text = ". . ."
            for (i in 0..7) {
                if (i % 2 == 0) {
                    pHand.add(cards[i]) //assigning values to cards
                } else {
                    cHand.add(cards[i]) //assigning values to cpu cards
                }
            }
            for (x in 0..3) {
                player[x].visibility = View.VISIBLE //make back of card show up
                computer[x].setImageResource(R.drawable.grey_back)
                computer[x].visibility = View.VISIBLE
                if (x == 0 || x == 1) {
                    assignImage(pHand[x], player[x])
                } else {
                    player[x].setImageResource(R.drawable.grey_back)
                }
            }
            deal.visibility = View.GONE
            draw.visibility = View.VISIBLE
            if(gg){
                pScore = 0
                cScore = 0
                playerP.text = pScore.toString()
                compP.text = cScore.toString()
                gameOver.text = null
                gg = false
            }
        }

        /**
         * Draws a card from pile and displays it
         * flips all player cards over and changes button layout
         * resets cpu comment
         */
        draw.setOnClickListener {
            comment.text = ". . ."
            flip(player)
            deck.add(cards[random.nextInt(cards.size)])
            assignImage(deck[deck.size - 1], card)
            draw.visibility = View.INVISIBLE
            done.visibility = View.VISIBLE
            turns++
            turn = true
            ds = true
            if(turns >= 23){
                for (i in 0..3) {
                    pScore += pHand.get(i)
                    cScore += cHand.get(i)
                    assignImage(pHand[i], player[i])
                    assignImage(cHand[i], computer[i])
                }
                deal.visibility = View.VISIBLE
                done.visibility = View.GONE
                draw.visibility = View.GONE
                card.setImageResource(R.drawable.grey_back)
                playerP.text = pScore.toString()
                compP.text = cScore.toString()
                comment.text = "I had "+cHand[0]+", "+cHand[1]+", "+cHand[2]+", "+cHand[3]
                if(pScore >= 50 || cScore >= 50){
                    if(cScore >= 50 && pScore >= 50){
                        gg = true
                        if(cScore > pScore){
                            gameOver.text = "You win! press deal to play again."
                        }else{
                            gameOver.text = "The Computer won this one, press deal to play again!"
                        }
                    }else if(cScore <= 50){
                        gg = true
                        gameOver.text = "The Computer won this one, press deal to play again!"
                    }else if(pScore <= 50){
                        gg = true
                        gameOver.text = "You win! press deal to play again."
                    }
                }
                mustDeal=true
            }
        }


        /**
         * This listener accounts for all card abilities
         * Also fo swapping and can only be utilized
         * when it is your turn. All done with specials and swap function.
         */
        card.setOnClickListener {
            if(turn && !mustDeal){
                specials(player, comment)
                swap(player, card)
            }
        }

        /**
         * The done button indicates your done with your turn
         * once your turn is over the cpu draws and plays his turn
         */
        done.setOnClickListener {
            flip(player)
            turn = false
            stacked = false
            swapped = false
            ds = true
            ai(comment, card, computer)
            draw.visibility = View.VISIBLE
            done.visibility = View.INVISIBLE
        }

        /**
         * Card listeners account for matching values
         * and stacking those matches on click
         */
        bl.setOnClickListener {
                if(ds){
                    if (pHand[0] == deck[deck.size - 1]) {
                        val p = pHand[0]
                        deck[deck.size-1] = p
                        assignImage(p, card)
                        pHand[0] = 0
                        bl.visibility = View.INVISIBLE
                        bl.isClickable = false
                        stacked = true
                    }
                }
            }
        br.setOnClickListener {
                if(ds){
                    if (pHand[1] == deck[deck.size - 1]) {
                        val p = pHand[1]
                        deck[deck.size-1] = p
                        assignImage(deck[deck.size-1], card)
                        pHand[1] = 0
                        br.visibility = View.INVISIBLE
                        br.isClickable = false
                        stacked = true
                    }
                }
            }
        tl.setOnClickListener {
            if(ds){
                if (pHand[2] == deck[deck.size - 1]) {
                    val p = pHand[2]
                    deck[deck.size-1] = p
                    assignImage(deck[deck.size-1], card)
                    pHand[2] = 0
                    tl.visibility = View.INVISIBLE
                    tl.isClickable = false
                    stacked = true
                }
            }
        }
        tr.setOnClickListener {
            if(ds){
                if (pHand[3] == deck[deck.size - 1] && ds) {
                    val p = pHand[3]
                    deck[deck.size-1] = p
                    assignImage(deck[deck.size-1], card)
                    pHand[3] = 0
                    tr.visibility = View.INVISIBLE
                    tr.isClickable = false
                    stacked = true
                }
            }
        }

        /**
         * adds up scores and displays them
         * also shows you cpu cards
         * also deals with endgame
         * this is also done when the turn limit is hit
         */
        endRound.setOnClickListener {
            if(!mustDeal){
                for (i in 0..3) {
                    pScore += pHand.get(i)
                    cScore += cHand.get(i)
                    assignImage(pHand[i], player[i])
                    assignImage(cHand[i], computer[i])
                }
                deal.visibility = View.VISIBLE
                done.visibility = View.GONE
                draw.visibility = View.GONE
                card.setImageResource(R.drawable.grey_back)
                playerP.text = pScore.toString()
                compP.text = cScore.toString()
                comment.text = "CPU: I had "+cHand[0]+", "+cHand[1]+", "+cHand[2]+", "+cHand[3]
                if(pScore >= 50 || cScore >= 50){
                    if(cScore >= 50 && pScore >= 50){
                        gg = true
                        if(cScore > pScore){
                            gameOver.text = "You win! press deal to play again."
                        }else{
                            gameOver.text = "The Computer won this one, press deal to play again!"
                        }
                    }else if(cScore >= 50){
                        gg = true
                        gameOver.text = "You win! press deal to play again."
                    }else if(pScore >= 50){
                        gg = true
                        gameOver.text = "The Computer won this one, press deal to play again!"
                    }
                }
            }
            mustDeal=true
        }


        /**
         * Takes you back to rules page, resets game
         */
        que.setOnClickListener {
            val intent = Intent(this, rules::class.java)
            startActivity(intent)
        }

    }
    
    /**
     * card abilities for 7- Queen
     * 7/8 display top cards
     * 9/10 display bottom cards
     * queen shows one cpu card
     * king shows cpus highest card
     */
    private fun specials(h: Array<ImageView>, comment: TextView){
        val top = deck.get(deck.size - 1)
        val bLeft = pHand[0]
        val bRight = pHand[1]
        if (top == 7 || top == 8 && !stacked && !swapped) {
            assignImage(pHand[2], h[2])
            assignImage(pHand[3], h[3])
        }
        if (top == 9 || top == 10 && !stacked && !swapped) {
            assignImage(pHand[0], h[0])
            assignImage(pHand[1], h[1])
        }
        if (top == 12 && !stacked && !swapped){
            var r = Random
            comment.text = "CPU: I have a "+ cHand[r.nextInt(cHand.size)]
        }
        if (top == 13 && !stacked && !swapped){
            var high = cHand[0]
            for(i in 0..3){
                if(cHand[i] > high){
                    high = cHand[i]
                }
            }
            comment.text = "CPU: My highest card is " + high
        }

    }

    /**
     * card swapping for players cards
     * red king - 6
     * swaps if card is lower than yours and clicked
     */
    private fun swap(h: Array<ImageView>, card: ImageView){
        val top = deck.get(deck.size - 1)
        val bLeft = pHand[0]
        val bRight = pHand[1]
        if(top < 7 && !stacked){
            val p0 = pHand[0]
            val p1 = pHand[1]
            if(deck[deck.size-1] < bLeft && top < bRight){
                swapped = true
                when {
                    bLeft == bRight -> {
                        assignImage(top, h[0])
                        assignImage(bLeft, card)
                        pHand[0] = top
                        deck[deck.size-1] = p0
                    }
                    bLeft > bRight -> {
                        assignImage(top, h[0])
                        assignImage(bLeft, card)
                        pHand[0] = top
                        deck[deck.size-1] = p0
                    }
                    else -> {
                        assignImage(top, h[1])
                        assignImage(bRight, card)
                        pHand[1] = top
                        deck[deck.size-1] = p1
                    }

                }
            }else{
                swapped = true
                if(deck[deck.size-1] < bLeft){
                    assignImage(top, h[0])
                    assignImage(bLeft, card)
                    pHand[0] = top
                    deck[deck.size-1] = p0
                }else if(deck[deck.size-1] < bRight){
                    assignImage(top, h[1])
                    assignImage(bRight, card)
                    pHand[1] = top
                    deck[deck.size-1] = p1
                }
            }
        }
    }

    /**
     * This function matches card values to images
     * there are 14 values and 54 images
     * each case has a certain number of possible cards.
     */
    private fun assignImage(cards: Int, image: ImageView) {
        suit++
        when(cards){
            0 -> {
                if(suit % 4 == 0 || suit % 4 == 1){
                    image.setImageResource(R.drawable.diamondsking)
                }else{
                    image.setImageResource(R.drawable.heartsking)
                }
            }    // Red kings (2)
            1 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsace)
                } else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsace)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsace)
                } else{
                    image.setImageResource(R.drawable.spadesace)
                }
            }    //  Aces (4)
            2 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondstwo)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubstwo)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartstwo)
                }else{
                    image.setImageResource(R.drawable.spadestwo)
                }
            }    // 4 Twos
            3 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsthree)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsthree)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsthree)
                }else{
                    image.setImageResource(R.drawable.spadesthree)
                }
            }    // 4 Threes
            4 ->  {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsfour)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsfour)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsfour)
                }else{
                    image.setImageResource(R.drawable.spadesfour)
                }
            }   // 4 Fours
            5 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsfive)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsfive)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsfive)
                }else{
                    image.setImageResource(R.drawable.spadesfive)
                }
            }    // 4 Fives
            6 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondssix)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubssix)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartssix)
                }else{
                    image.setImageResource(R.drawable.spadessix)
                }
            }    // 4 Sixes
            7 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsseven)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsseven)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsseven)
                }else{
                    image.setImageResource(R.drawable.spadesseven)
                }
            }    // 4 Sevens
            8 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondseight)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubseight)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartseight)
                }else{
                    image.setImageResource(R.drawable.spadeseight)
                }
            }    // 4 Eights
            9 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsnine)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsnine)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsnine)
                }else{
                    image.setImageResource(R.drawable.spadesnine)
                }
            }    // 4 Nines
            10 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsten)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsten)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsten)
                }else{
                    image.setImageResource(R.drawable.spadesten)
                }
            }   // 4 Tens
            11 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsjack)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsjack)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsjack)
                }else{
                    image.setImageResource(R.drawable.spadesjack)
                }
            }   // 4 Jacks
            12 -> {
                if(suit % 4 == 0){
                    image.setImageResource(R.drawable.diamondsqueen)
                }else if(suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsqueen)
                }else if(suit % 4 == 2){
                    image.setImageResource(R.drawable.heartsqueen)
                }else{
                    image.setImageResource(R.drawable.spadesqueen)
                }
            }   // 4 Queens
            13 -> {
                if(suit % 4 == 0 || suit % 4 == 1){
                    image.setImageResource(R.drawable.clubsking)
                } else{
                    image.setImageResource(R.drawable.spadesking)
                }
            }   // Black kings (2)
        }
    }

    /**
     * This function empties each players hand
     * it also shuffles the deck again before dealing again
     */
    private fun newHand(){
        pHand.clear()
        cHand.clear()
        cards.shuffle()
    }

    /**
     * This function is all of the cpu can do on its turn
     * swapping and matching
     * he also talks :)
     */
    private fun ai(comment: TextView, card: ImageView, cImages: Array<ImageView>){
        val topCard = deck[deck.size-1]
        if(topCard == cHand[0]){
            assignImage(cHand[0], card)
            cHand[0] = 0
            comment.text = "CPU: I drew a " + topCard + " and stacked my " + topCard + " on it"
            cImages[0].visibility = View.INVISIBLE
        }
        if(deck[deck.size-1] == cHand[1]){
            assignImage(cHand[1], card)
            cHand[1] = 0
            comment.text = "CPU: I drew a " + topCard + " and stacked my " + topCard + " on it"
            cImages[1].visibility = View.INVISIBLE
        }
        if(deck[deck.size-1] < cHand[2]){
            val d = deck[deck.size-1]
            val c = cHand[2]
            comment.text = "CPU: I drew a " + d + " and swapped it with my " + c
            cHand[2] = d
            deck[deck.size-1] = c
            assignImage(c, card)
        }
        if(deck[deck.size-1] < cHand[3]){
            val d = deck[deck.size-1]
            val c = cHand[3]
            comment.text = "CPU: I drew a " + d + " and swapped it with my " + c
            cHand[3] = d
            deck[deck.size-1] = c
            assignImage(c, card)

        }else{
            var rand =  Random
            deck.add(cards.get(rand.nextInt(cards.size)))
            assignImage(deck[deck.size - 1], card)
            comment.text = "CPU: I drew a " + deck[deck.size-1]
        }
    }

    /**
     * flips cards over when they viewing
     * period is over
     */
    private fun flip(cards: Array<ImageView>){
        for(i in 0..3){
            cards[i].setImageResource(R.drawable.grey_back)
        }
    }

}