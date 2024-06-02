import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "./ui/table";
import { Data } from "./header";
import { format } from "date-fns";

interface LoanTableProps extends React.ComponentPropsWithoutRef<"table"> {
  data: Data;
}

export default function LoanTable({ data, ...props }: LoanTableProps) {
  const formatNumber = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
  });

  return (
    <Table {...props} className="mt-8 block max-h-[636px] overflow-auto">
      <TableHeader className="w-full">
        <TableRow className="bg-zinc-100">
          <TableHead>Data Competência</TableHead>
          <TableHead className="w-full">Valor de Empréstimo</TableHead>
          <TableHead>Saldo Devedor</TableHead>
          <TableHead>Consolidada</TableHead>
          <TableHead>Total</TableHead>
          <TableHead>Amortização</TableHead>
          <TableHead>Saldo</TableHead>
          <TableHead>Provisão</TableHead>
          <TableHead>Acumulado</TableHead>
          <TableHead>Pago</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {data.payments.map((payment) => (
          <TableRow key={payment.date.valueOf()}>
            <TableCell>
              {format(new Date(payment.date.replace(/-/g, "/")), "dd/MM/yyyy")}
            </TableCell>
            <TableCell>{formatNumber.format(payment.value)}</TableCell>
            <TableCell>
              {formatNumber.format(payment.remainingBalance)}
            </TableCell>
            <TableCell>
              {payment.consolidated !== -1
                ? `${payment.consolidated}/${data.parcels}`
                : ""}
            </TableCell>
            <TableCell>{formatNumber.format(payment.total)}</TableCell>
            <TableCell>{formatNumber.format(payment.amortization)}</TableCell>
            <TableCell>{formatNumber.format(payment.debit)}</TableCell>
            <TableCell>{formatNumber.format(payment.provision)}</TableCell>
            <TableCell>{formatNumber.format(payment.accumulated)}</TableCell>
            <TableCell>{formatNumber.format(payment.paid)}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
