
<@override name="title">私信列表 - 智乎</@override>
<@extends name="header.ftl"></@extends>
<link rel="stylesheet" href="../styles/letter.css">
    <div id="main">
        <div class="zg-wrap zu-main clearfix ">
            <ul class="letter-list">
                <#list conversations as conversation>
                <li id="conversation-item-10001_622873">
                <a class="letter-link" href="/msg/detail?conversationId=${conversation.conversation.conversationId}">
                </a>
                <div class="letter-info">
                <span class="l-time"> ${conversation.conversation.createdDate?string('yyyy-MM-dd HH:mm:ss')}</span>
                <div class="l-operate-bar">

                <a href="/msg/detail?conversationId=${conversation.conversation.conversationId}">
                    共${conversation.conversation.id}条会话
                </a>
                </div>
                </div>
                <div class="chat-headbox">
                <span class="msg-num">
                ${conversation.unRead}
                </span>
                <a class="list-head">
                <img alt="头像" src="${conversation.user.headUrl}">
                </a>
                </div>
                <div class="letter-detail">
                <a title="${conversation.user.name}" class="letter-name level-color-1" href="/user/${conversation.user.id}">
                    ${conversation.user.name}
                </a>
                <p class="letter-brief">
                <a href="/msg/detail?conversationId=${conversation.conversation.conversationId}">
                ${conversation.conversation.content}
                </a>
                </p>
                </div>
                </li>
                </#list>
            </ul>

        </div>
    </div>

<@extends name="footer.ftl"></@extends>
<script type="text/javascript" src="/scripts/main/site/detail.js"></script>
</body></html>