
<@override name="title">私信列表 - 智乎</@override>
<@extends name="header.ftl"></@extends>
<link rel="stylesheet" href="../styles/letter.css">

<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <ul class="letter-chatlist">
            <#list messages as message>
            <li id="msg-item-4009580">
                <a class="list-head" style="margin-top: 0px">
                    <img alt="头像" src="${message.headUrl}">
                </a>
                <div class="tooltip fade right in">
                    <div class="tooltip-arrow"></div>
                    <div class="tooltip-inner letter-chat clearfix">
                        <div class="letter-info">
                            <p class="letter-time">${message.message.createdDate?string("yyyy-MM-dd hh:mm:ss")}</p>
                            <!-- <a href="javascript:void(0);" id="del-link" name="4009580">删除</a> -->
                        </div>
                        <p class="chat-content">
                            ${message.message.content}
                        </p>
                    </div>
                </div>
            </li>
            </#list>
        </ul>

    </div>
</div>

<@extends name="footer.ftl"></@extends>
<script type="text/javascript" src="/scripts/main/site/detail.js"></script>
</body></html>