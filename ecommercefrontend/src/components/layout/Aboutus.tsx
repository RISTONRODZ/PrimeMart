const AboutUs = () => {
    return (
        <div className="bg-white text-gray-800">

            <section className="bg-blue-700 text-white py-24 px-6 text-center">
                <h1 className="text-4xl sm:text-5xl font-bold mb-4 leading-tight">
                    Built for real shopping.<br />By a real developer.
                </h1>
                <p className="text-blue-200 text-lg max-w-xl mx-auto leading-relaxed">
                    PrimeMart is a full-stack e-commerce platform built from scratch —
                    with a Spring Boot backend, React frontend, and everything in between.
                </p>
            </section>
            <section className="py-16 px-6">
                <div className="max-w-2xl mx-auto text-center">
                    <p className="text-blue-700 text-sm font-medium tracking-widest uppercase mb-3">
                        Why I built this
                    </p>
                    <h2 className="text-2xl font-bold text-gray-900 mb-4">
                        A portfolio project that works like the real thing
                    </h2>
                    <p className="text-gray-500 leading-relaxed">
                        I'm Riston, a Computer Engineering student at SFIT Mumbai. PrimeMart was built to
                        demonstrate real-world backend and frontend skills — JWT authentication, payment
                        integration with Razorpay, an AI-powered product assistant, and a clean,
                        responsive UI. Every feature you see is fully functional.
                    </p>
                </div>
            </section>
            <section className="pb-16 px-6">
                <div className="max-w-2xl mx-auto">
                    <p className="text-center text-sm text-gray-400 mb-4 uppercase tracking-widest font-medium">
                        Tech stack
                    </p>
                    <div className="flex flex-wrap justify-center gap-2">
                        {[
                            "Spring Boot", "PostgreSQL", "Spring Security", "JWT",
                            "React", "TypeScript", "Tailwind CSS", "Razorpay",
                            "Spring AI", "Junit 5","Mockito","Docker", "GitHub Actions","Material UI"
                        ].map((tech) => (
                            <span
                                key={tech}
                                className="bg-blue-50 text-blue-700 border border-blue-100 text-sm px-3 py-1 rounded-full"
                            >
                {tech}
              </span>
                        ))}
                    </div>
                </div>
            </section>

            <section className="bg-blue-700 text-white py-14 px-6 text-center">
                <h2 className="text-2xl font-bold mb-3">See it in action</h2>
                <p className="text-blue-200 mb-6 max-w-sm mx-auto">
                    Browse products, place an order, or check out the seller dashboard.
                </p>
                <div className="flex flex-col sm:flex-row gap-3 justify-center">
                    <button className="bg-white text-blue-700 font-semibold px-6 py-2.5 rounded-lg hover:bg-blue-50 transition-colors">
                        Shop now
                    </button>
                    <button className="border border-white text-white font-semibold px-6 py-2.5 rounded-lg hover:bg-white/10 transition-colors">
                        Become a seller
                    </button>
                </div>
            </section>

        </div>
    );
};

export default AboutUs;