package com.oi.hata

import HataTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.oi.hata.common.ui.LocalBackPressedDispatcher
import com.oi.hata.ui.HataApp
import com.oi.hata.ui.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HataTheme {
                HataApp(homeViewModel = homeViewModel)
            }
        }
    }

   /*

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var cognitoCachingCredentialsProvider: CognitoCachingCredentialsProvider

    @Inject
    lateinit var  hataRemoteDataSource: HataRemoteDataSource

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HataTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SignIn()
                }
            }
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }*/

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task?.getResult(
                ApiException::class.java
            )

            val googleFirstName = account?.givenName ?: ""
            val googleEmail = account?.email ?: ""
            val googleProfilePicURL = account?.photoUrl.toString()
            val googleIdToken = account?.idToken ?: ""

            Log.d("Token >>>><",googleIdToken)

            CoroutineScope(Dispatchers.IO).launch {
                refreshAwsCredentials("Google",googleIdToken)
                hello()
            }

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString() + e.toString()
            )
        }

    }

    suspend  fun refreshAwsCredentials(provider: String, token: String){

        var logins = mutableMapOf<String,String>()

        withContext(Dispatchers.IO) {
            if(provider.equals("Google")){
                logins.put("accounts.google.com",token)
            }

            cognitoCachingCredentialsProvider.withLogins(logins)
            cognitoCachingCredentialsProvider.refresh()


        }

    }

    suspend fun hello(){
        withContext(Dispatchers.IO){
            hataRemoteDataSource.hello()
        }
    }

    @Composable
    fun SignIn() {
        /*Button(onClick = {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(
                signInIntent, RC_SIGN_IN
            )
        }) {
            androidx.compose.material.Text(text = "Sign In")
        }*/

        val startForResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Sign In",">>>>>>>>>>>>>>>>>>>>>>>>>")
                val data = result.data
                // Handle the Intent
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }

        Button(onClick = {
            val signInIntent = googleSignInClient.signInIntent
            startForResult.launch(signInIntent)
        }) {
            androidx.compose.material.Text(text = "Sign In")
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        HataTheme {
            SignIn()
        }
    }


    */





}

