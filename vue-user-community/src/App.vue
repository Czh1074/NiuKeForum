<!-- <template>
  <header>
  <div class="nk-container">
    <div>首页</div>
    <div>消息</div>
    <div>注册</div>
  </div>
  </header>

</template>



<script>


</script>

<style scoped>

 @import '../static/css/global.css';
 @import url(https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css);


 .nk-container {
  height: 50px;
  width: 100%;
  background-color: darkgrey;
 }
</style> -->




<template>
  <div class="container-fluid">
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <!-- 网站头像 -->
        <a class="navbar-brand" href="#">
          <i class="fas fa-user-circle fa-2x"></i>
        </a>
        <!-- 菜单 -->
        <button class="navbar-toggler" type="button"
          data-toggle="collapse" data-target="#navbarNav"
          aria-controls="navbarNav" aria-expanded="false"
          aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item">
              <a class="nav-link" href="#">首页</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">消息</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">注册</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">登录</a>
            </li>
          </ul>
          <!-- 个人头像 -->
          <div class="dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button"
              id="dropdownMenuLink" data-toggle="dropdown"
              aria-haspopup="true" aria-expanded="false">
              <i class="fas fa-user-circle fa-2x"></i>
            </a>
            <div class="dropdown-menu dropdown-menu-right"
              aria-labelledby="dropdownMenuLink">
              <a class="dropdown-item" href="#">个人中心</a>
              <a class="dropdown-item" href="#">退出</a>
            </div>
          </div>
          <!-- 搜索栏 -->
          <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search"
              placeholder="搜索" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0"
              type="submit">搜索</button>
          </form>
        </div>
      </div>
    </nav>

    <!-- 中间内容部分
    <div class="row mt-3">
      <div class="col-md-8 offset-md-2">
        <div class="card mb-3" v-for="post in posts" :key="post.id">
          <div class="card-body">
            <div class="media">
              <img :src="post.avatar" class="mr-3 rounded-circle"
                :alt="post.author">
              <div class="media-body">
                <h5 class="mt-0">{{ post.title }}</h5>
                <p class="mb-1">{{ post.content }}</p>
                <small class="text-muted">{{ post.author }} 发布于
                  {{ post.date }}</small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div> -->

    <!-- 底部 -->
    <footer class="bg-light mt-3 p-3">
      <div class="container text-center">
        <p>版权所有 © 2023 某某公司</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { posts } from "@/data/posts.js"; // 导入模拟数据

export default {
  name: "App",
  data() {
    return {
      posts: posts,
    };
  },
};
</script>

<style>
</style>
