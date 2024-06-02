import { useState } from "react";
import LoanForm from "./loan-form";
import LoanTable from "./loan-table";

type HeaderProps = React.ComponentPropsWithoutRef<"header">;

export interface Payment {
  date: string;
  value: number;
  remainingBalance: number;
  total: number;
  amortization: number;
  debit: number;
  provision: number;
  accumulated: number;
  paid: number;
  consolidated: number;
}

export interface Data {
  payments: Payment[];
  parcels: number;
}

export default function Header(props: HeaderProps) {
  const [data, setData] = useState<Data | null>(null);

  return (
    <header {...props}>
      <h1 className="text-2xl font-black mb-4">Calculadora de Empréstimos</h1>

      <LoanForm setData={setData} />

      {data && data.payments ? (
        <LoanTable data={data} className="mt-8" />
      ) : null}
    </header>
  );
}
