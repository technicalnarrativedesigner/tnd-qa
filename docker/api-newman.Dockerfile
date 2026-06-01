FROM node:20-slim

WORKDIR /app/api/postman

COPY api/postman/package*.json /app/api/postman/
RUN npm install

COPY api/postman/ /app/api/postman/
COPY docker/run-newman.sh /usr/local/bin/run-newman.sh
RUN chmod +x /usr/local/bin/run-newman.sh

CMD ["/usr/local/bin/run-newman.sh"]
