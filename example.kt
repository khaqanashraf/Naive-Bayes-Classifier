fun main() {
    val ham = arrayOf(
    	"Ok lar... Joking wif u oni...",
        "Go until jurong point, crazy.. Available only in bugis n great world la e buffet... Cine there got amore wat...",
        "U dun say so early hor... U c already then say...",
        "Nah I don't think he goes to usf, he lives around here though",
        "Even my brother is not like to speak with me. They treat me like aids patent."
    )
    val spam = arrayOf(
    	"Free entry in 2 a wkly comp to win FA Cup final tkts 21st May 2005. Text FA to 87121 to receive entry question(std txt rate)T&C's apply 08452810075over18's",
        "FreeMsg Hey there darling it's been 3 week's now and no word back! I'd like some fun you up for it still? Tb ok! XxX std chgs to send, Â£1.50 to rcv",
        "WINNER!! As a valued network customer you have been selected to receivea Â£900 prize reward! To claim call 09061701461. Claim code KL341. Valid 12 hours only.",
        "Had your mobile 11 months or more? U R entitled to Update to the latest colour mobiles with camera for Free! Call The Mobile Update Co FREE on 08002986030",
        "SIX chances to win CASH! From 100 to 20,000 pounds txt> CSH11 and send to 87575. Cost 150p/day, 6days, 16+ TsandCs apply Reply HL 4 info"
    )
    
    
    val classifier = BayesClassifier()
    
    ham.forEach{
        classifier.updateCorpus(it, BayesClassifier.CLASS_POSITIVE)
    }
    spam.forEach{
        classifier.updateCorpus(it, BayesClassifier.CLASS_NEGATIVE)
    }
    
    val new = "I'm back &amp; we're packing the car now, I'll let you know if there's room" 
    
    println(classifier.predictClass(new))
    
    
}
