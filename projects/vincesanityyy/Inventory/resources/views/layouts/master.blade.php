
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="x-ua-compatible" content="ie=edge">
  <meta name="csrf-token" content="{{ csrf_token() }}">
  <title>Inventory gago</title>

  <link rel="stylesheet" href="{{asset('css/app.css')}}">
</head>
<body class="hold-transition sidebar-mini">
<div id="app" class="wrapper">

  <!-- Navbar -->
  <navbar-notifications user={{Auth::user()->name}}></navbar-notifications>
  
  <!-- /.navbar -->

  <!-- Main Sidebar Container -->
  <aside class="main-sidebar sidebar-no-expand sidebar-dark-primary elevation-4">
    <!-- Brand Logo -->
    <a href="index3.html" class="brand-link text-sm">
      <img src="{{asset('img/logo.png')}}" alt="AdminLTE Logo" class="brand-image img-circle elevation-3"
           style="opacity: .8">
      <span class="brand-text font-weight-light">Inventory</span>
    </a>

    <!-- Sidebar -->
    <div class="sidebar no-expand">
      <!-- Sidebar user panel (optional) -->
      <div class="user-panel mt-3 pb-3 mb-3 d-flex">
        <div class="image">
          <img src="{{('img/logo.png')}}" class="img-circle elevation-2" alt="User Image">
        </div>
        <div class="info">
          <a href="#" class="d-block"> {{ Auth::user()->name }}</a>
        </div>
      </div>

      <!-- Sidebar Menu -->
      <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column nav-child-indent nav-compact nav-legacy nav-flat text-sm" data-widget="treeview" role="menu" data-accordion="false">
            <li class="nav-item has-treeview">
                <a href="#" class="nav-link">
                  <i class="nav-icon fas fa-box"></i>
                  <p>
                    Inventory
                    <i class="fas fa-angle-left right"></i>
                  </p>
                </a>
                <ul class="nav nav-treeview" style="display: none;">
                  <li class="nav-item">
                    <router-link to="/products" class="nav-link">
                      <i class="far fa-circle nav-icon"></i>
                      <p>Products</p>
                    </router-link>
                  </li>
                  <li class="nav-item">
                    <router-link to="/suppliers" class="nav-link">
                      <i class="far fa-circle nav-icon"></i>
                      <p>Suppliers</p>
                    </router-link>
                  </li>
                  <li class="nav-item">
                    <router-link to="/history" class="nav-link">
                      <i class="far fa-circle nav-icon"></i>
                      <p>Stock History</p>
                    </router-link>
                  </li>
                  <li class="nav-item">
                    <router-link to="/reports" class="nav-link">
                      <i class="far fa-circle nav-icon"></i>
                      <p>Reports</p>
                    </router-link>
                </ul>
            </li>
            <li class="nav-item has-treeview">
                <a href="#" class="nav-link">
                  <i class="nav-icon fas fa-tree"></i>
                  <p>
                    Extra Menu
                    <i class="fas fa-angle-left right"></i>
                  </p>
                </a>
                <ul class="nav nav-treeview" style="display: none;">
                  <li class="nav-item">
                    <a href="pages/UI/general.html" class="nav-link">
                      <i class="far fa-circle nav-icon"></i>
                      <p>Products</p>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="pages/UI/icons.html" class="nav-link">
                      <i class="far fa-circle nav-icon"></i>
                      <p>Suppliers</p>
                    </a>
                  </li>
                </ul>
            </li>
            <li class="nav-item">
                <a href="pages/widgets.html" class="nav-link">
                  <i class="nav-icon fas fa-th"></i>
                  <p>
                    Another Extra
                    <span class="right badge badge-danger">New</span>
                  </p>
                </a>
            </li>
            <li class="nav-item">
                <a href="{{ route('logout') }}" class="nav-link"
                onclick="event.preventDefault();
                document.getElementById('logout-form').submit();">
                  <i class="nav-icon fas fa-times"></i>
                  <p>
                    Log out
                  </p>
                </a>
                <form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
                  @csrf
              </form>
            </li>
        </ul>
      </nav>
      <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
  </aside>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Main content -->
    <div class="content">
      <div  class="container-fluid">
       <router-view></router-view>
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

  <!-- Main Footer -->
  <footer class="main-footer text-sm">
    <!-- To the right -->
    <div class="float-right d-none d-sm-inline">
      Anything you want
    </div>
    <!-- Default to the left -->
    <strong>Copyright &copy; 2014-2019 <a href="https://pornhub.com">Footer diri</a>.</strong> All rights reserved.
  </footer>
</div>
<script src="{{asset('js/app.js')}}"></script>
{{-- <script src="/js/app.js"></script> --}}
</body>
</html>
