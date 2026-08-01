import {useState} from "react";
import {useSearchParams} from "react-router-dom";
import LoginForm from "./LoginForm.tsx";
import SignupForm from "./SignupForm.tsx";
import {Button} from "@mui/material";
const Auth = () => {
    const [searchParams] = useSearchParams();
    const [isLogin, setIsLogin] = useState(searchParams.get('mode') !== 'signup');
    return (
       <div className={'flex flex-col items-center min-h-screen pt-16 pb-16'}>
            <div className={'max-w-md w-full h-auto bg-white rounded-lg shadow-lg p-6'}>
                <img className={'w-full rounded-t-md'} alt={'banner'} src={'src/assets/signup_banner.png'} />
                <div className={'mt-8 px-10'}>
                    {isLogin ? <LoginForm /> : <SignupForm />}
                    <div className={'flex items-center gap-1 justify-center mt-5'}>
                        <p>{isLogin ? 'Don\'t have an account' : 'Already have an account'}</p>
                        <Button variant={'text'} onClick={() => setIsLogin(!isLogin)}>{isLogin ? 'Sign Up' : 'Log In'}</Button>
                    </div>
                </div>
            </div>
        </div>
    );
};
export default Auth;