############################################################

# 閺嬪嫬缂撻崺铏诡攨闂�婊冨剼centos7+jdk-8u121-linux-x64+experiencecenter

############################################################

#鐠佸墽鐤嗛崺铏诡攨闂�婊冨剼

FROM registry.dk:5000/c7j8

#鐎规矮绠熸担婊嗭拷锟�
MAINTAINER xuhuanjun "xuhuanjun@yixincapital.com"

#娴溠冩惂閻楀牊婀�
ENV DISTRO_NAME=${project.artifactId}-${version}.jar
#閻滎垰顣ㄩ弮鍫曟？
RUN rm -rf /etc/localtime && ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
#闁劎璁查張宥呭
COPY ./$DISTRO_NAME /
RUN chmod +x /$DISTRO_NAME
ADD ./docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
#閸旂姴鍙嗛弬鍥ㄣ�傛潪顒佸床閺堝秴濮熼惄纾嬬箾闁板秶鐤�--閺傚洦銆傛潪顒佸床閺堝秴濮熼弰鐥篿ndows閻滎垰顣ㄩ敍灞炬￥濞夋槴ocker閸栨牓锟斤拷
#婵″倹鐏夋担璺ㄦ暏濞村鐦惃鍕暈閸愬奔鑵戣箛鍐跨礉濞戝寮烽崚鏉款樋濞夈劌鍞芥稉顓炵妇闂傤噣顣介敍灞惧娴犮儵鍣伴悽銊ф纯鏉╋拷
#COPY ./dubbo-resolve.properties /root/dubbo-resolve.properties
#閺嗘挳婀剁粩顖氬經
EXPOSE 18106
#鐠佸墽鐤嗛弮銉ョ箶閸楋拷
VOLUME ["/logs"]
ENTRYPOINT ["/bin/sh","/docker-entrypoint.sh"]
