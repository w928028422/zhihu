<#macro answer_question vo>
<div class="feed-item folding feed-item-hook feed-item-2
                        " feed-item-a="" data-type="a" id="feed-2" data-za-module="FeedItem" data-za-index="">
    <meta itemprop="ZReactor" data-id="389034" data-meta="{&quot;source_type&quot;: &quot;promotion_answer&quot;, &quot;voteups&quot;: 4168, &quot;comments&quot;: 69, &quot;source&quot;: []}">
    <div class="feed-item-inner">
        <div class="avatar">
            <a title="${vo.userName}" data-tip="p$t$amuro1230" class="zm-item-link-avatar" target="_blank" href="/user/${vo.userId}">
                <img src="${vo.userHead}" class="zm-item-img-avatar"></a>
        </div>
        <div class="feed-main">
            <div class="feed-content" data-za-module="AnswerItem">
                <meta itemprop="answer-id" content="389034">
                <meta itemprop="answer-url-token" content="13174385">

                <div class="expandable entry-body">

                    <div class="zm-item-answer-author-info">
                        <a class="author-link" data-tip="p$b$amuro1230" target="_blank" href="/user/${vo.userId}">${vo.userName}</a>
                        回答了该问题 ${vo.createdDate?string('yyyy-MM-dd HH:mm:ss')}</div>

                    <div class="zm-item-rich-text expandable js-collapse-body" data-resourceid="123114" data-action="/answer/content" data-author-name="李淼" data-entry-url="/question/19857995/answer/13174385">
                        <div class="zh-summary summary clearfix"><a href="/question/${vo.questionId}">${vo.questionTitle}</a></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</#macro>

<#macro follow_question vo>
<div class="feed-item folding feed-item-hook feed-item-2
                        " feed-item-a="" data-type="a" id="feed-2" data-za-module="FeedItem" data-za-index="">
    <meta itemprop="ZReactor" data-id="389034" data-meta="{&quot;source_type&quot;: &quot;promotion_answer&quot;, &quot;voteups&quot;: 4168, &quot;comments&quot;: 69, &quot;source&quot;: []}">
    <div class="feed-item-inner">
        <div class="avatar">
            <a title="${vo.userName}" data-tip="p$t$amuro1230" class="zm-item-link-avatar" target="_blank" href="/user/${vo.userId}">
                <img src="${vo.userHead}" class="zm-item-img-avatar"></a>
        </div>
        <div class="feed-main">
            <div class="feed-content" data-za-module="AnswerItem">
                <meta itemprop="answer-id" content="389034">
                <meta itemprop="answer-url-token" content="13174385">

                <div class="expandable entry-body">

                    <div class="zm-item-answer-author-info">
                        <a class="author-link" data-tip="p$b$amuro1230" target="_blank" href="/user/${vo.userId}">${vo.userName}</a>
                        关注了该问题 ${vo.createdDate?string('yyyy-MM-dd HH:mm:ss')}</div>

                    <div class="zm-item-rich-text expandable js-collapse-body" data-resourceid="123114" data-action="/answer/content" data-author-name="李淼" data-entry-url="/question/19857995/answer/13174385">
                        <div class="zh-summary summary clearfix"><a href="/question/${vo.questionId}">${vo.questionTitle}</a></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</#macro>

<#macro like_question vo>
<div class="feed-item folding feed-item-hook feed-item-2
                        " feed-item-a="" data-type="a" id="feed-2" data-za-module="FeedItem" data-za-index="">
    <meta itemprop="ZReactor" data-id="389034" data-meta="{&quot;source_type&quot;: &quot;promotion_answer&quot;, &quot;voteups&quot;: 4168, &quot;comments&quot;: 69, &quot;source&quot;: []}">
    <div class="feed-item-inner">
        <div class="avatar">
            <a title="${vo.userName}" data-tip="p$t$amuro1230" class="zm-item-link-avatar" target="_blank" href="/user/${vo.userId}">
                <img src="${vo.userHead}" class="zm-item-img-avatar"></a>
        </div>
        <div class="feed-main">
            <div class="feed-content" data-za-module="AnswerItem">
                <meta itemprop="answer-id" content="389034">
                <meta itemprop="answer-url-token" content="13174385">

                <div class="expandable entry-body">

                    <div class="zm-item-answer-author-info">
                        <a class="author-link" data-tip="p$b$amuro1230" target="_blank" href="/user/${vo.userId}">${vo.userName}</a>
                        赞了该回答 ${vo.createdDate?string('yyyy-MM-dd HH:mm:ss')}</div>

                    <div class="zm-item-rich-text expandable js-collapse-body" data-resourceid="123114" data-action="/answer/content" data-author-name="李淼" data-entry-url="/question/19857995/answer/13174385">
                        <div class="zh-summary summary clearfix"><a href="/question/${vo.questionId}">${vo.answerName}</a></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</#macro>



