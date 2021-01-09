#!/usr/bin/env sh

## 基准目录
BASE_DIR=`cd "$(dirname $0)/.." >/dev/null; pwd`

## 引入setenv.sh中的自定义变量
source "$BASE_DIR/bin/setenv.sh"

## 全局变量
CONFIG_PATH="$BASE_DIR/config"  #配置文件目录
LOGBACK="logback-spring.xml"

function start()
{
  cd "$BASE_DIR"

  # 判断应用是否已经在运行
  appId=`ps -ef | grep java | grep "$JAR_FILE" |awk '{print $2}'`
  if [[ ! -z "$appId" ]];then
    error "Maybe $APP_NAME is running, please check it..."
    return 1
  fi

  # 获取配置文件路径
  PROFILE=$1
  if get_config_path "$PROFILE"; then
    log "The config path will be：$CONFIG_PATH/"
  else
    return 1
  fi

  # 启动命令的配置、日志参数
  CONFIG_OPTS="-Dspring.config.location=$CONFIG_PATH/"
  LOGGING_OPTS="-Dlogging.config=$CONFIG_PATH/$LOGBACK"

  CMD="nohup java $JVM_OPTS $CONFIG_OPTS $LOGGING_OPTS -jar $JAR_FILE  >/dev/null 2>&1 &"

  echo CMD

  log "The $APP_NAME is starting with: \n\n$CMD\n"
  eval exec "$CMD"

  appId=`ps -ef | grep java | grep "$JAR_FILE" |awk '{print $2}'`
  if [[ -z "$appId" ]];then
    error "Failed to start, please check it..."
    return 1
  else
    log "The pid is: $appId"
  fi
}

function stop()
{
  appId=`ps -ef | grep java | grep "$JAR_FILE" |awk '{print $2}'`
  if [[ -z "$appId" ]];then
    error "Maybe $APP_NAME not running, please check it..."
    return 1
  fi

  check_times=1
  # Try to kill it gracefully three times
  for i in 1 .. 3
  do
    kill "$appId"
    for j in 1 .. 5
    do
      log "The $APP_NAME is stopping...$check_times"
      let check_times++
      sleep 1
      appId=`ps -ef | grep java | grep "$JAR_FILE" |awk '{print $2}'`
      if [[ -z "$appId" ]];then
        log "The $APP_NAME is stopped!"
        return 0
      fi
    done
  done

  # Kill it by force
  kill -9 "$appId"

  # Double check if it is stopped
  for i in 1 .. 15
  do
    log "The $APP_NAME is stopping...$check_times"
    let check_times++
    sleep 1
    appId=`ps -ef | grep java | grep "$JAR_FILE" |awk '{print $2}'`
    if [[ -z "$appId" ]];then
      log "The $APP_NAME is stopped!"
      return 0
    fi
  done

  error "The $APP_NAME can not be stopped in ${check_times}s, please check it."
  return 1
}

function restart()
{
  stop

  start $1

  return 0
}

function status()
{
  appId=`ps -ef | grep java | grep "$JAR_FILE" |awk '{print $2}'`
  if [[ -z "$appId" ]];then
    log "Not running"
  else
    log "Running [$appId]"
  fi

  return 0
}

## 获取配置文件路径
function get_config_path()
{
  # 如果设置了profile，配置文件路径为：config/$profile；否则配置文件路径为：config
  PROFILE=$1

  if [[ -n "$PROFILE" ]];then
    if [[ -d "$CONFIG_PATH/$PROFILE" ]];then
      CONFIG_PATH="$CONFIG_PATH/$PROFILE"
    else
      error "Profile is set to: $PROFILE, but can not find the path：$CONFIG_PATH/$PROFILE/"
      return 1
    fi
  fi

  return 0
}


function log()
{
  echo -e "`date +\"%Y-%m-%d %T\"` [INFO ] $1"
}

function warn()
{
  echo -e "`date +\"%Y-%m-%d %T\"` [WARN ] \033[33m$1\033[0m"
}

function error()
{
  echo -e "`date +\"%Y-%m-%d %T\"` [ERROR] \033[31m$1\033[0m"
}

function usage()
{
  echo "Usage: $0 {start [dev|sit|uat|prd] | stop | restart | status}"
  echo "Example: $0 start dev"
  exit 1
}

## 查找lib目录下的以$APP_NAME(定义在setenv.sh中)开头的jar文件，用于start、stop、status等操作
count=`ls "$BASE_DIR/lib" | grep "$APP_NAME.*.jar$" | wc -l`
if [[ "$count" -eq 0 ]]; then
  error "Can not find any jar like $APP_NAME under $BASE_DIR/lib."
  exit 1
elif [[ "$count" -gt 1 ]]; then
  error "There are $count jars like $APP_NAME under $BASE_DIR/lib, should be only one."
  exit 1
fi
FILE_NAME=`ls "$BASE_DIR/lib" | grep "$APP_NAME.*.jar$"`
JAR_FILE="$BASE_DIR/lib/$FILE_NAME"

## 启动参数
case $1 in
  start)
  start $2;;

  stop)
  stop;;

  restart)
  restart $2;;

  status)
  status;;

  *)
  usage;;
esac