import { useForm } from "react-hook-form";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "./ui/form";
import { DatePicker } from "./ui/date-picker";
import { Data } from "./header";
import { toast } from "sonner";

const formSchema = z
  .object({
    initialDate: z.date({ required_error: "Campo não preenchido" }),
    endDate: z.date({ required_error: "Campo não preenchido" }),
    firstPaymentDate: z.date({ required_error: "Campo não preenchido" }),
    value: z.number({
      required_error: "Campo não preenchido",
      invalid_type_error: "Precisa ser um número",
    }),
    interestRate: z.number({
      required_error: "Campo não preenchido",
      invalid_type_error: "Precisa ser um número",
    }),
  })
  .refine((data) => data.endDate > data.initialDate, {
    message: "Data final precisa ser maior que a data inicial",
    path: ["endDate"],
  })
  .refine((data) => data.firstPaymentDate > data.initialDate, {
    message: "Primeiro pagamento precisa ser depois da data inicial",
    path: ["firstPaymentDate"],
  })
  .refine((data) => data.firstPaymentDate < data.endDate, {
    message: "Primeiro pagamento precisa ser antes da data final",
    path: ["firstPaymentDate"],
  });

interface LoanFormProps {
  setData: React.Dispatch<React.SetStateAction<Data | null>>;
}

export default function LoanForm({ setData }: LoanFormProps) {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      initialDate: undefined,
      endDate: undefined,
      firstPaymentDate: undefined,
      value: undefined,
      interestRate: undefined,
    },
  });

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    await fetch("http://localhost:8080/loan/calculate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        initialDate: values.initialDate,
        endDate: values.endDate,
        firstPaymentDate: values.firstPaymentDate,
        value: Number(values.value),
        interestRate: Number(values.interestRate),
      }),
    })
      .then(async (res) => {
        setData(await res.json());

        toast.success("Empréstimo calculado com sucesso!");
      })
      .catch(() => {
        toast.success("Erro ao calcular empréstimo.");
      });
  };

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="flex flex-col xl:flex-row gap-4 xl:items-start"
      >
        <FormField
          control={form.control}
          name="initialDate"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Data Inicial</FormLabel>
              <FormControl>
                <DatePicker onDateChange={field.onChange} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="endDate"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Data Final</FormLabel>
              <FormControl>
                <DatePicker onDateChange={field.onChange} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="firstPaymentDate"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Primeiro Pagamento</FormLabel>
              <FormControl>
                <DatePicker onDateChange={field.onChange} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="value"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Valor do Empréstimo</FormLabel>
              <FormControl>
                <Input
                  {...field}
                  onChange={(e) =>
                    form.setValue(field.name, Number(e.target.value))
                  }
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="interestRate"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Taxa de Juros</FormLabel>
              <FormControl>
                <Input
                  {...field}
                  onChange={(e) =>
                    form.setValue(field.name, Number(e.target.value))
                  }
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <Button type="submit" className="xl:mt-[32px]">
          Calcular
        </Button>
      </form>
    </Form>
  );
}
