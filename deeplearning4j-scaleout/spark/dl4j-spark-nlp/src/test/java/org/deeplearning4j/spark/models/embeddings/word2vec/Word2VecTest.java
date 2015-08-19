/*
 *
 *  * Copyright 2015 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package org.deeplearning4j.spark.models.embeddings.word2vec;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author jeffreytang
 */
public class Word2VecTest {

    @Test
    public void testConcepts() throws Exception {
        // These are all default values for word2vec
        SparkConf sparkConf = new SparkConf().setMaster("local[4]").setAppName("sparktest");

        // Set SparkContext
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        // Path of data
        String dataPath = new ClassPathResource("raw_sentences.txt").getFile().getAbsolutePath();

        // Read in data
        JavaRDD<String> corpus = sc.textFile(dataPath);

        Word2Vec word2Vec = new Word2Vec().setNumWords(1).setnGrams(1)
                .setTokenizer("org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory")
                .setTokenPreprocessor("org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor")
                .setRemoveStop(true)
                .setSeed(42L)
                .setNegative(0)
                .setUseAdaGrad(false)
                .setVectorLength(100)
                .setWindow(5)
                .setAlpha(0.025).setMinAlpha(0.0001)
                .setIterations(1);
        ;
        Pair<VocabCache, WeightLookupTable> table = word2Vec.train(corpus);
        WordVectors vectors = WordVectorSerializer.fromPair(new Pair<>((InMemoryLookupTable) table.getSecond(),
                                                                       table.getFirst()));
//        Collection<String> words = vectors.wordsNearest("day", 10);
//        System.out.println(Arrays.toString(words.toArray()));
//
//        assertTrue(words.contains("week"));
    }


}