package com.zhihu.Service;

import com.zhihu.Model.Question;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/zhihu";
    private HttpSolrClient solrClient = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    public List<Question> searchQuestion(String keyword, int offset, int count,
                                         String hlpre, String hlpos) throws Exception{
        List<Question> questionList = new ArrayList<>();
        String queryWord = QUESTION_TITLE_FIELD + ":" + keyword + "\n" + QUESTION_CONTENT_FIELD + ":" + keyword;
        SolrQuery query = new SolrQuery(queryWord);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlpre);
        query.setHighlightSimplePost(hlpos);
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
        QueryResponse response = solrClient.query(query);
        for(Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()){
            Question question = new Question();
            question.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)){
                List<String> contents = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if(contents.size() > 0){
                    question.setContent(contents.get(0));
                }
            }
            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)){
                List<String> titles = entry.getValue().get(QUESTION_TITLE_FIELD);
                if(titles.size() > 0){
                    question.setTitle(titles.get(0));
                }
            }
            questionList.add(question);
        }
        return questionList;
    }

    public boolean indexQuestion(int questionId, String title, String content) throws Exception{
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", questionId);
        document.setField("question_title", title);
        document.setField("question_content", content);
        UpdateResponse response = solrClient.add(document, 1000);
        return response != null && response.getStatus() == 0;
    }
}
