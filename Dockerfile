FROM babashka/babashka:alpine as bb

WORKDIR /opt/site

COPY bb.edn .
COPY src src/
COPY public public/

RUN bb src/site.clj

FROM node:alpine3.17 as n

WORKDIR /opt/site

COPY package.json .
COPY package-lock.json .

RUN npm i


COPY tailwind.config.js .
COPY --from=bb /opt/site/src src/
COPY --from=bb /opt/site/public public/

RUN npm run build

FROM arm64v8/nginx

COPY --from=n /opt/site/public /usr/share/nginx/html
