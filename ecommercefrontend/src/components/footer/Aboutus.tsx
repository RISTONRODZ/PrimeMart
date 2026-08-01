import {Link} from "react-router-dom";

const AboutUs = () => {
    return (
        <div className="bg-white text-gray-800">
            <section className="bg-blue-700 text-white py-24 px-6 text-center">
                <h1 className="text-4xl sm:text-5xl font-bold mb-4 leading-tight">
                    A full-stack e-commerce platform,<br />built end to end.
                </h1>
                <p className="text-blue-200 text-lg max-w-xl mx-auto leading-relaxed">
                    PrimeMart is a production-style shopping platform — Spring Boot backend,
                    React frontend, and everything in between.
                </p>
            </section>

            <section className="py-16 px-6">
                <div className="max-w-2xl mx-auto text-center">
                    <p className="text-blue-700 text-sm font-medium tracking-widest uppercase mb-3">
                        Why I built this
                    </p>
                    <h2 className="text-2xl font-bold text-gray-900 mb-4">
                        Every feature here is fully functional
                    </h2>
                    <p className="text-gray-500 leading-relaxed mb-6">
                        I'm Riston, a Computer Engineering student at SFIT Mumbai. I built PrimeMart
                        to go beyond tutorials — JWT-based auth, Razorpay payment integration, an
                        AI product assistant with RAG, and a CI pipeline running real tests.
                    </p>
                    <div className="flex justify-center gap-4 text-sm font-medium">
                        <a href="https://github.com/RISTONRODZ/Ecommerce" target="_blank" rel="noopener noreferrer"
                           className="text-blue-700 hover:underline">
                            GitHub →
                        </a>
                        <a href="https://www.linkedin.com/in/ristonrodrigues/" target="_blank" rel="noopener noreferrer"
                           className="text-blue-700 hover:underline">
                            LinkedIn →
                        </a>
                    </div>
                </div>
            </section>

            <section className="pb-16 px-6">
                <p className="text-center text-sm text-gray-400 mb-4 uppercase tracking-widest font-medium">
                    Tech stack
                </p>
                <div className="max-w-2xl mx-auto flex flex-wrap justify-center gap-2">
                    {[
                        "Spring Boot", "PostgreSQL", "Spring Security", "JWT",
                        "React", "TypeScript", "Tailwind CSS", "Razorpay",
                        "Spring AI", "JUnit 5", "Mockito", "Docker", "GitHub Actions", "Material UI"
                    ].map((tech) => (
                        <span key={tech}
                              className="bg-blue-50 text-blue-700 border border-blue-100 text-sm px-3 py-1 rounded-full">
                            {tech}
                        </span>
                    ))}
                </div>
            </section>

            <section className="bg-blue-700 text-white py-14 px-6 text-center">
                <h2 className="text-2xl font-bold mb-3">See it in action</h2>
                <p className="text-blue-200 mb-6 max-w-sm mx-auto">
                    Browse products, place an order, or check out the seller dashboard.
                </p>
                <div className="flex flex-col sm:flex-row gap-3 justify-center">
                    <Link to="/products" className="bg-white text-blue-700 font-semibold px-6 py-2.5 rounded-lg hover:bg-blue-50 transition-colors">
                        Shop now
                    </Link>
                    <Link to="/seller/register" className="border border-white text-white font-semibold px-6 py-2.5 rounded-lg hover:bg-white/10 transition-colors">
                        Become a seller
                    </Link>
                </div>
            </section>
        </div>
    );
};
export default AboutUs;