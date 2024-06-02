import Header from "./components/header";
import { Toaster } from "@/components/ui/sonner";

export default function App() {
  return (
    <>
      <div className="container mx-auto flex flex-col py-24">
        <Header />
      </div>
      <Toaster richColors position="top-center" />
    </>
  );
}
