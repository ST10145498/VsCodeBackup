using Microsoft.AspNetCore.Mvc;
using Firebase.Auth;
using Newtonsoft.Json;
using MathApp.Models;

public class AuthController : Controller
{
    private readonly FirebaseAuthProvider _auth;

  public AuthController(IConfiguration config)
{
    var apiKey = config["Firebase:ApiKey"];
    _auth = new FirebaseAuthProvider(new FirebaseConfig(apiKey));
}

    [HttpGet]
    public IActionResult Login()
    {
        return View();
    }

    [HttpPost]
    public async Task<IActionResult> Login(LoginModel login)
    {
        try
        {
            var fbAuthLink = await _auth.SignInWithEmailAndPasswordAsync(login.Email, login.Password);
            string currentUserId = fbAuthLink.User.LocalId;
        
            if (currentUserId != null)
            {
                HttpContext.Session.SetString("currentUser", currentUserId);
                return RedirectToAction("Calculate", "Math");
            }
        }
        catch (FirebaseAuthException ex)
        {
            var firebaseEx = JsonConvert.DeserializeObject<FirebaseErrorModel>(ex.ResponseData);
            ModelState.AddModelError(string.Empty, firebaseEx.error.message);
            return View(login);
        }

        return View();
    }

    [HttpGet]
    public IActionResult LogOut()
    {
        HttpContext.Session.Remove("currentUser");
        return RedirectToAction("Login");
    }

    [HttpGet]
    public IActionResult Register()
    {
        return View();
    }

    [HttpPost]
    public async Task<IActionResult> Register(LoginModel login)
    {
        try
        {
            await _auth.CreateUserWithEmailAndPasswordAsync(login.Email, login.Password);
            var fbAuthLink = await _auth.SignInWithEmailAndPasswordAsync(login.Email, login.Password);
            string currentUserId = fbAuthLink.User.LocalId;

            if (currentUserId != null)
            {
                HttpContext.Session.SetString("currentUser", currentUserId);
                return RedirectToAction("Calculate", "Math");
            }
        }
        catch (FirebaseAuthException ex)
        {
            var firebaseEx = JsonConvert.DeserializeObject<FirebaseErrorModel>(ex.ResponseData);
            ModelState.AddModelError(string.Empty, firebaseEx.error.message);
            return View(login);
        }

        return View();
    }
}