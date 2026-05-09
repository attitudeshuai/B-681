# 构建阶段
FROM node:16-alpine AS build

WORKDIR /app

# 设置国内镜像源加速
RUN npm config set registry https://registry.npmmirror.com

# 复制package.json并安装依赖
COPY package*.json ./
RUN npm install

# 复制源代码并构建
COPY . .
RUN npm run build

# 生产阶段
FROM nginx:alpine

# 复制构建产物
COPY --from=build /app/dist /usr/share/nginx/html

# 复制Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 3000

# 启动Nginx
CMD ["nginx", "-g", "daemon off;"]
