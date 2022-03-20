pipeline {
  agent any
  stages {
    stage('检出') {
      steps {
        checkout([$class: 'GitSCM', branches: [[name: env.GIT_BUILD_REF]], 
                                            userRemoteConfigs: [[url: env.GIT_REPO_URL, credentialsId: env.CREDENTIALS_ID]]])
      }
    }
    stage('构建') {
      steps {
        echo '构建中...'
        sh 'mvn clean package'
        echo '构建完成.'
        archiveArtifacts(artifacts: 'let-search-web/target/let-search-web-1.0-SNAPSHOT.jar', fingerprint: true)
        echo '上传到制品库'
      }
    }
    stage('分发jar包') {
      steps {
        script {
          echo '分发中...'
          withCredentials([sshUserPrivateKey(credentialsId: '616f6277-6771-4763-8144-31cb536139c7', \
          keyFileVariable: 'SSH_KEY_FOR_ABC', \
          passphraseVariable: '', \
          usernameVariable: '')]) {
            sh "echo ${SSH_KEY_FOR_ABC}"
            echo 'upload'
            sh 'scp -i ${SSH_KEY_FOR_ABC} let-search-web/target/let-search-web-1.0-SNAPSHOT.jar root@121.199.1.16:/home/icsearch/java/'
            echo '开始部署'
            sh "ssh -i ${SSH_KEY_FOR_ABC} root@121.199.1.16 'systemctl restart let-search'"
            echo '部署成功'
            
          }
        }

      }
    }
  }
}