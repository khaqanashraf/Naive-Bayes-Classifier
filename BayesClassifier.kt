class BayesClassifier{
    private var vocabSize = 0
    
    private var positiveSum = 0
    private var negativeSum = 0

    private var positiveClassProbability = 0.0
    private var negativeClassProbability = 0.0

    private val positiveCorpus = mutableMapOf<String, Int>()
    private val negativeCorpus = mutableMapOf<String, Int>()

    private fun calculateParams(){
        positiveSum = positiveCorpus.values.sum()
        negativeSum = negativeCorpus.values.sum()
        positiveClassProbability = positiveSum.toDouble()/(positiveSum+negativeSum)
        negativeClassProbability = negativeSum.toDouble()/(positiveSum+negativeSum)
        vocabSize = positiveCorpus.size + negativeCorpus.size
    }

    fun getCorpus(selectedClass: Int): Map<String, Int>{
        when(selectedClass){
            CLASS_POSITIVE-> return positiveCorpus
            CLASS_NEGATIVE-> return negativeCorpus
            else-> return mapOf()
        }
    }
    
    fun getWordsFrequency(text: String): Map<String, Int>{
        val words = text.toLowerCase().split(" ").map { Regex("[^a-zA-Z]").replace( it , "") } as MutableList<String>
        words.removeAll(Classifier.englishStopWords)
        val corpus = words.groupingBy{ it }
            .eachCount()

        return corpus
    }

    fun addIntoCorpus(wf: Map<String, Int>, selectedClass:Int){
        wf.forEach{
            val word = it.key
            val frequency = it.value
            when(selectedClass){
                CLASS_POSITIVE->{
                    positiveCorpus[word] = positiveCorpus.getOrPut(word){0} + frequency
                }
                CLASS_NEGATIVE->{
                    
                    negativeCorpus[word] = negativeCorpus.getOrPut(word){0} + frequency
                }
            }

        }
        calculateParams()
    }
    
    fun updateCorpus(wf: Map<String, Int>, selectedClass: Int){
        addIntoCorpus(wf, selectedClass)
    }
    fun updateCorpus(text: String, selectedClass: Int){
        val wf = getWordsFrequency(text)
        addIntoCorpus(wf, selectedClass)
    }

    fun probabilityOfWordByClass(word: String, selectedClass: Int): Double{

        when(selectedClass){
            CLASS_POSITIVE-> return (1.0+positiveCorpus.getOrPut(word){0})/(positiveSum+vocabSize)
            CLASS_NEGATIVE-> return (1.0+negativeCorpus.getOrPut(word){0})/(negativeSum+vocabSize)
            else-> return 0.0
        }
    }

    fun liklyhoodEstimation(wf: Map<String, Int>, selectedClass: Int): Double{
        var LE = 1.0
        wf.forEach{
            val word = it.key
            val frequency = it.value
            val pr_of_word = Math.pow(probabilityOfWordByClass(word, selectedClass), frequency.toDouble())
            LE *= pr_of_word
        }
        when(selectedClass){
            CLASS_POSITIVE-> LE *= positiveClassProbability
            CLASS_NEGATIVE-> LE *= negativeClassProbability

        }

        return LE
    }

    fun predictClass(text: String): Int{
        val wf = getWordsFrequency(text)
        val POSITIVE_LIKLYHOOD = liklyhoodEstimation(wf, CLASS_POSITIVE)
        val NEGATIVE_LIKLYHOOD = liklyhoodEstimation(wf, CLASS_NEGATIVE)

        if(POSITIVE_LIKLYHOOD>NEGATIVE_LIKLYHOOD)
            return CLASS_POSITIVE
        return CLASS_NEGATIVE

    }

    fun getTokens( text : String ) : Array<String> {
        val tokens = text.toLowerCase().split( " " )
        val filteredTokens = ArrayList<String>()
        for ( i in 0 until tokens.count() ) {
            if ( !tokens[i].trim().isBlank() ) {
                filteredTokens.add( tokens[ i ] )
            }
        }
        val stopWordRemovedTokens = tokens.map { Regex("[^a-zA-Z]").replace( it , "") }
                as ArrayList<String>
        stopWordRemovedTokens.removeAll {
            englishStopWords.contains( it ) or it.trim().isBlank()
        }
        return stopWordRemovedTokens.distinct().toTypedArray()
    }




    companion object {
        val CLASS_POSITIVE = 0 // Spam
        val CLASS_NEGATIVE = 1 // Ham
        val englishStopWords = arrayOf(
            "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves",
            "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their",
            "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was",
            "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the",
            "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against",
            "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in",
            "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why",
            "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no",
            "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should",
            "now" , "hello" , "hi" , "dear" , "respected" , "thank"
        )

    }

}
